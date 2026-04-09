package top.song_mojing.nest.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.BatteryManager
import android.os.PowerManager
import android.util.Log
import top.song_mojing.nest.manager.StateManager

object BatteryReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent?) {
		// 电量变更
		val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
		val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
		if (level != -1 && scale != -1) {
			val batteryPct = (level / scale.toFloat() * 100).toInt()
			if (StateManager.batteryLevel.intValue != batteryPct) {
				StateManager.batteryLevel.intValue = batteryPct
				Log.d("Service", "电量变更: $batteryPct%")
			}
		}
		// 省电模式变更
		val pm = context?.getSystemService(POWER_SERVICE) as PowerManager
		val isModeOn = pm.isPowerSaveMode
		if (StateManager.isPowerSaveMode.value != isModeOn) {
			StateManager.isPowerSaveMode.value = isModeOn
			Log.d("Service", "省电模式状态变更: $isModeOn")
		}
	}
}