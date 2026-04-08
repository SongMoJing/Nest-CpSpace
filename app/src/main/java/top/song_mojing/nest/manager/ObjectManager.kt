package top.song_mojing.nest.manager

import kotlinx.serialization.json.Json
import top.song_mojing.nest.models.Companion
import java.io.File

object ObjectManager {

	var companion: Companion? = Companion(
		name = "Song_Mojing",
		nickname = "松蓦箐",
		sex = "Male",
		birthday = null
	)
	var self: Companion? = null
	var setting: SettingManager = SettingManager()

	fun load(configDir: File): Boolean {
		if (!configDir.exists()) {
			configDir.mkdirs()
			return false
		}
		if (!configDir.isDirectory) {
			configDir.deleteRecursively()
			return false
		}
		val companionFile = File(configDir, "companion.json")
		val selfFile = File(configDir, "self.json")
		val settingsFile = File(configDir, "settings.json")
		companion = loadFile<Companion>(companionFile)
		self = loadFile<Companion>(selfFile)
		loadFile<SettingManager>(settingsFile)?.let {
			setting = it
		}
		return self != null
	}

	private val json = Json {
		ignoreUnknownKeys = true
		coerceInputValues = true
	}

	/**
	 * 泛型辅助函数：读取并解析单个 JSON 文件
	 */
	private inline fun <reified T> loadFile(file: File): T? {
		return if (file.exists() && file.isFile) {
			try {
				val content = file.readText()
				json.decodeFromString<T>(content)
			} catch (e: Exception) {
				e.printStackTrace() // 实际开发建议使用 Log.e
				null
			}
		} else {
			null
		}
	}
}