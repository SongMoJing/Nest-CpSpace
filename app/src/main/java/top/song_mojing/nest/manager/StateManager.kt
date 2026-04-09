package top.song_mojing.nest.manager

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

object StateManager {
	var locationProvider: SnapshotStateList<String> = mutableStateListOf()
	var location: MutableState<Location> = mutableStateOf(Location("").apply { reset()	})
	var batteryLevel = mutableIntStateOf(-1)
	var isPowerSaveMode = mutableStateOf(false)
}