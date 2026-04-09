package top.song_mojing.nest.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import top.song_mojing.nest.android.service.CoreCommunicationService


const val ACTION_STOP_LOCATION_SERVICE = "ACTION_STOP_LOCATION_SERVICE"
const val ACTION_START_LOCATION_SERVICE = "ACTION_START_LOCATION_SERVICE"

const val COMMAND_STOP_LOCATION = "COMMAND_STOP_LOCATION"
const val COMMAND_START_LOCATION = "COMMAND_START_LOCATION"


class NotificationActionReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		when (intent.action) {
			ACTION_STOP_LOCATION_SERVICE -> {
				val stopLocIntent = Intent(context, CoreCommunicationService::class.java).apply {
					action = COMMAND_STOP_LOCATION
				}
				context.startService(stopLocIntent)
			}
			ACTION_START_LOCATION_SERVICE -> {
				val startLocIntent = Intent(context, CoreCommunicationService::class.java).apply {
					action = COMMAND_START_LOCATION
				}
				context.startService(startLocIntent)
			}
		}
	}
}
