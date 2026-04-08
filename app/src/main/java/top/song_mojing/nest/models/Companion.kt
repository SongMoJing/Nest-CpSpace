package top.song_mojing.nest.models

class Companion {

	private var _name: String
	private var _nickname: String?
	private var _sex: String

	/**
	 * 名字
	 */
	val name: String
		get() = _name

	/**
	 * 昵称
	 */
	val nickname: String
		get() = _nickname ?: _name

	/**
	 * 性别
	 */
	val sex: String
		get() = _sex

	constructor(
		name: String,
		nickname: String?,
		sex: String
	) {
		_name = name
		_nickname = nickname
		_sex = sex
	}
}