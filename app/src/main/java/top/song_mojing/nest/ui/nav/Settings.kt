package top.song_mojing.nest.ui.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import top.song_mojing.nest.R
import top.song_mojing.nest.manager.ObjectManager
import top.song_mojing.nest.models.Gender
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Settings(
	modifier: Modifier = Modifier
) {
	val scrollState = rememberScrollState()

	Column(
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(scrollState)
			.padding(15.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Spacer(modifier = Modifier.height(18.dp))
		UserInfoCard(
			username = ObjectManager.self?.name ?: stringResource(R.string.string_unknown),
			birthday = ObjectManager.self?.birthday ?: Date(),
			gender = ObjectManager.self?.gender ?: Gender.Unknown
		)
		Text(
			text = stringResource(R.string.nav_settings_item_general),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.primary,
			modifier = Modifier.padding(start = 4.dp, top = 8.dp)
		)
		Column {
			SettingsListItem(
				icon = Icons.Default.Notifications,
				title = stringResource(R.string.nav_settings_item_general_notification),
				onClick = {}
			)
			SettingsListItem(
				icon = Icons.Default.BatteryChargingFull,
				title = stringResource(R.string.nav_settings_item_general_power),
				onClick = {}
			)
			HorizontalDivider(
				modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
				thickness = 0.5.dp,
				color = MaterialTheme.colorScheme.outlineVariant
			)
			SettingsListItem(
				icon = Icons.Default.Info,
				title = stringResource(R.string.nav_settings_item_general_about),
				onClick = {}
			)
		}
	}
}

@Composable
fun UserInfoCard(
	username: String,
	gender: Gender,
	birthday: Date
) {
	ElevatedCard(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.elevatedCardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		)
	) {
		val genderColor = when (gender) {
			Gender.Male -> Color(0xFF1E88E5)
			Gender.Female -> Color(0xFFEA6FBD)
			Gender.Unknown -> Color(0xFF9E9E9E)
		}
		Column(
			modifier = Modifier
				.padding(22.dp)
				.fillMaxWidth(),
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = username,
					style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
				)
				Icon(
					when (gender) {
						Gender.Male -> Icons.Default.Male
						Gender.Female -> Icons.Default.Female
						Gender.Unknown -> Icons.Default.Person
					},
					contentDescription = null,
					modifier = Modifier.size(24.dp),
					tint = genderColor
				)
				IconButton(
					onClick = { },
					modifier = Modifier.size(27.dp)
				) {
					Icon(
						modifier = Modifier.scale(0.7f),
						imageVector = Icons.Default.Edit,
						contentDescription = null,
						tint = Color.Gray
					)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(birthday),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
fun SettingsListItem(
	icon: ImageVector,
	title: String,
	onClick: () -> Unit,
) {
	ListItem(
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = 48.dp)
			.clip(MaterialTheme.shapes.medium)
			.clickable { onClick() },
		headlineContent = {
			Text(title, style = MaterialTheme.typography.bodyLarge)
		},
		leadingContent = {
			Icon(
				imageVector = icon,
				contentDescription = null,
				modifier = Modifier.size(20.dp),
				tint = MaterialTheme.colorScheme.onSurface
			)
		},
		trailingContent = {
			Icon(
				imageVector = Icons.Default.ChevronRight,
				contentDescription = null,
				modifier = Modifier.size(16.dp),
				tint = MaterialTheme.colorScheme.outline
			)
		},
		colors = ListItemDefaults.colors(containerColor = Color.Transparent)
	)
}