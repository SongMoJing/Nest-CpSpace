package top.song_mojing.nest.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import top.song_mojing.nest.R
import top.song_mojing.nest.manager.ObjectManager
import top.song_mojing.nest.models.Companion
import top.song_mojing.nest.models.Gender
import top.song_mojing.nest.ui.EmbeddedDatePicker
import top.song_mojing.nest.ui.EmbeddedInputBox
import top.song_mojing.nest.ui.EmbeddedSelector
import java.util.Date

@Composable
fun PersonalInfo(
	onClose: () -> Unit = {},
	onConfirm: (Companion) -> Unit
) {
	var nameText by remember { mutableStateOf(ObjectManager.self?.name ?: "") }
	var gender by remember { mutableStateOf(ObjectManager.self?.gender ?: Gender.Male) }
	val genderColor = when (gender) {
		Gender.Male -> Color(0xFF1E88E5)
		Gender.Female -> Color(0xFFEA6FBD)
		Gender.Unknown -> Color(0xFF9E9E9E)
	}
	var date: Date? by remember { mutableStateOf(ObjectManager.self?.birthday) }

	val male = stringResource(R.string.string_gender_male)
	val female = stringResource(R.string.string_gender_female)
	val unknown = stringResource(R.string.string_unknown)
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 32.dp, vertical = 36.dp)
			.navigationBarsPadding()
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Bottom
		) {
			Text(
				text = stringResource(R.string.welcome_guidance_name_0),
				style = MaterialTheme.typography.titleLarge
			)
			Spacer(modifier = Modifier.width(5.dp))
			EmbeddedInputBox(
				hint = stringResource(R.string.welcome_guidance_name_hint),
				value = nameText,
				onValueChange = { nameText = it }
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Bottom
		) {
			Text(
				text = stringResource(R.string.welcome_guidance_gender_0),
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.width(5.dp))
			Icon(
				when (gender) {
					Gender.Male -> Icons.Default.Male
					Gender.Female -> Icons.Default.Female
					Gender.Unknown -> Icons.Default.Person
				},
				tint = genderColor,
				contentDescription = null,
				modifier = Modifier.size(20.dp)
			)
			Spacer(modifier = Modifier.width(5.dp))
			EmbeddedSelector(
				hint = stringResource(R.string.welcome_guidance_gender_hint),
				selectedValue = stringResource(gender.id),
				options = listOf(male, female, unknown),
				onSelected = {
					gender = when (it) {
						male -> Gender.Male
						female -> Gender.Female
						else -> Gender.Unknown
					}
				}
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Bottom
		) {
			Text(
				text = stringResource(R.string.welcome_guidance_birthday_0),
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.width(5.dp))
			EmbeddedDatePicker(
				hint = stringResource(R.string.welcome_guidance_birthday_hint),
				selectedDate = date,
				onDateSelected = { date = it }
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Bottom
		) {
			TextButton(
				onClick = onClose,
				modifier = Modifier.weight(1f)
			) {
				Text(stringResource(R.string.string_close))
			}
			Spacer(modifier = Modifier.width(5.dp))
			Button(
				onClick = {
					onConfirm(
						Companion(
							name = nameText,
							nickname = null,
							gender = gender,
							birthday = date
						)
					)
					onClose()
				},
				modifier = Modifier.weight(1f)
			) {
				Text(stringResource(R.string.string_confirm))
			}
		}
	}
}