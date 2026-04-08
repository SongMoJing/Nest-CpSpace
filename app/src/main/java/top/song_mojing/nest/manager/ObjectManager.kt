package top.song_mojing.nest.manager

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import top.song_mojing.nest.models.Companion
import java.io.File
import kotlin.properties.Delegates

object ObjectManager {

	private var currentConfigDir: File? = null

	var companion: Companion? by Delegates.observable(null) { _, _, _ ->
		saveToDisk("companion.json", companion)
	}
	var self: Companion? by Delegates.observable(null) { _, _, _ ->
		saveToDisk("self.json", self)
	}
	var setting: SettingManager by Delegates.observable(SettingManager()) { _, _, _ ->
		saveToDisk("settings.json", setting)
	}

	fun load(configDir: File): Boolean {
		currentConfigDir = configDir
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
		companion = loadFile<Companion>(companionFile) ?: companion
		self = loadFile<Companion>(selfFile)
		loadFile<SettingManager>(settingsFile)?.let {
			setting = it
		}
		return self != null
	}

	private val json = Json {
		ignoreUnknownKeys = true
		coerceInputValues = true
		prettyPrint = true
	}

	private inline fun <reified T> saveToDisk(fileName: String, data: T?) {
		val dir = currentConfigDir ?: return
		if (data == null) return

		try {
			val file = File(dir, fileName)
			val content = json.encodeToString(data)
			file.writeText(content)
		} catch (e: Exception) {
			e.printStackTrace()
		}
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