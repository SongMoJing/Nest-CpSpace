package top.song_mojing.nest.models

import kotlinx.serialization.Serializable
import top.song_mojing.nest.utils.DateSerializer
import java.util.Date

@Serializable
class Companion(
	var name: String,
	var nickname: String?,
	var sex: String,
	@Serializable(with = DateSerializer::class)
	var birthday: Date?
) {
}