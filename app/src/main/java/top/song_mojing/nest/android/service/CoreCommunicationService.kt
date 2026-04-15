package top.song_mojing.nest.android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import top.song_mojing.nest.R
import top.song_mojing.nest.android.receiver.ACTION_START_LOCATION_SERVICE
import top.song_mojing.nest.android.receiver.ACTION_STOP_LOCATION_SERVICE
import top.song_mojing.nest.android.receiver.BatteryReceiver
import top.song_mojing.nest.android.receiver.COMMAND_STOP_LOCATION
import top.song_mojing.nest.android.receiver.NotificationActionReceiver
import top.song_mojing.nest.android.sql.AppDatabase
import top.song_mojing.nest.android.sql.model.LocationInformation
import top.song_mojing.nest.manager.StateManager
import top.song_mojing.nest.ui.activity.MainActivity

class CoreCommunicationService : Service() {

	private var locationManager: LocationManager? = null
	private var locationServices: LocationServices? = null
	private val channelId = "pure_android_location_channel"

	private val stopLocationService by lazy {
		PendingIntent.getBroadcast(
			this, 1, Intent(this, NotificationActionReceiver::class.java).apply { action = ACTION_STOP_LOCATION_SERVICE },
			PendingIntent.FLAG_IMMUTABLE
		)
	}
	private val startLocationService by lazy {
		PendingIntent.getBroadcast(
			this, 2, Intent(this, NotificationActionReceiver::class.java).apply { action = ACTION_START_LOCATION_SERVICE },
			PendingIntent.FLAG_IMMUTABLE
		)
	}
	private val activityIntent by lazy {
		val intent = Intent(this, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
		}
		PendingIntent.getActivity(
			this,
			2,
			intent,
			PendingIntent.FLAG_IMMUTABLE
		)
	}

	private val db by lazy {
		AppDatabase.getDatabase(this)
	}

	private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

	override fun onCreate() {
		super.onCreate()
		locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
		createNotificationChannel()
		registerReceiver(BatteryReceiver, IntentFilter().apply {
			addAction(Intent.ACTION_BATTERY_CHANGED)
			addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
		})
		serviceScope.launch {
			snapshotFlow {
				Pair(
					StateManager.batteryLevel.intValue,
					StateManager.locationProvider.toList()
				)
			}
				.distinctUntilChanged()
				.scan(null as Pair<Int, List<String>>?) { old, new ->
					if (old != null) {
						if (old.first != new.first) {
							Log.d("Service", "变化源: 电量 ${old.first} -> ${new.first}")
						}
						if (old.second != new.second) {
							Log.d("Service", "变化源: 定位列表 ${old.second} -> ${new.second}")
							if (new.second.isNotEmpty()) {
								recordLocation(LocationInformation.EVENT_TYPE_START)
							} else {
								recordLocation(LocationInformation.EVENT_TYPE_STOP)
							}
						}
					}
					new
				}
				.collect {
					updateNotification()
				}
		}
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		if (intent?.action == COMMAND_STOP_LOCATION) {
			locationServices?.let {
				locationManager?.removeUpdates(it)
			}
			locationServices = null
			StateManager.locationProvider.clear()
			updateNotification()
			return START_STICKY
		}
		updateNotification()
		locationServices = locationManager?.let {
			LocationServices(this, it)
		}
		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? = null

	private fun createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelId, "原生定位后台服务", NotificationManager.IMPORTANCE_LOW
			)
			val manager = getSystemService(NotificationManager::class.java)
			manager?.createNotificationChannel(channel)
		}
	}

	fun updateNotification() {
		val contentText = if (StateManager.locationProvider.isEmpty()) {
			getString(R.string.service_running_locationProvider_disable)
		} else {
			getString(R.string.service_running_locationProvider_enable)
		}
		val notification = NotificationCompat.Builder(this, channelId)
			.setContentTitle(getString(R.string.service_running))
			.setContentText("${contentText}\n当前电量：${StateManager.batteryLevel.intValue}%")
			.setSmallIcon(R.mipmap.ic_launcher_round)
			.setOngoing(true)
			.setContentIntent(activityIntent)
			.addAction(
				android.R.drawable.ic_menu_manage,
				when (StateManager.locationProvider.isEmpty()) {
					true -> getString(R.string.service_running_locationProvider_action_enable)
					false -> getString(R.string.service_running_locationProvider_action_disable)
				},
				when (StateManager.locationProvider.isEmpty()) {
					true -> startLocationService
					false -> stopLocationService
				}
			)
			.build()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
			startForeground(
				1001,
				notification,
				ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC or ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
			)
		} else {
			startForeground(1001, notification)
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		recordLocation(LocationInformation.EVENT_TYPE_STOP)
		serviceScope.cancel()
		unregisterReceiver(BatteryReceiver)
		locationServices?.let { locationManager?.removeUpdates(it) }
	}

	fun recordLocation(
		event: Int,
		latitude: Double = 0.0,
		longitude: Double = 0.0,
		accuracy: Float = 0f,
	) {
		val stopNode = LocationInformation(
			latitude = latitude,
			longitude = longitude,
			accuracy = accuracy,
			eventType = event
		)
		runBlocking(Dispatchers.IO) {
			db.locationDao().insert(stopNode)
		}
	}
}