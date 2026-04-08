package top.song_mojing.nest.manager

import top.song_mojing.nest.models.Companion

object ObjectManager {
	var companion: Companion? = Companion(
		name = "Song_Mojing",
		nickname = "松蓦箐",
		sex = "Male"
	)
	var self: Companion? = null
	val setting: SettingManager = SettingManager()
}