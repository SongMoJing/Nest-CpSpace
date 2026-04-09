package top.song_mojing.nest.models

import top.song_mojing.nest.R

enum class Gender(val id: Int) {
	Male(R.string.string_gender_male),
	Female(R.string.string_gender_female),
	Unknown(R.string.string_unknown)
}