package top.song_mojing.nest.android.service

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import top.song_mojing.nest.manager.StateManager

class LocationServices(
	locationManager: LocationManager
) : LocationListener {

	init {
		try {
			val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
			val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
			if (isGpsEnabled) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this)
				StateManager.locationProvider.add(LocationManager.GPS_PROVIDER)
			}
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, this)
				StateManager.locationProvider.add(LocationManager.NETWORK_PROVIDER)
			}
			if (!isGpsEnabled && !isNetworkEnabled) {
				Log.e("CoreCommunicationService", "所有定位提供者均不可用，请检查系统设置")
			}
		} catch (e: SecurityException) {
			Log.e("CoreCommunicationService", "缺少权限", e)
		}
	}

	override fun onLocationChanged(location: Location) {
		Log.d("CoreCommunicationService", "原生定位成功: 纬度 ${location.latitude}, 经度 ${location.longitude}")
		StateManager.location.value = location
	}

	override fun onProviderEnabled(provider: String) {
		if (!StateManager.locationProvider.any { it == provider }) {
			StateManager.locationProvider.add(provider)
		}
		Log.d("CoreCommunicationService", "定位提供者已启用: $provider")
	}

	override fun onProviderDisabled(provider: String) {
		StateManager.locationProvider.removeAll { it == provider }
	}
}