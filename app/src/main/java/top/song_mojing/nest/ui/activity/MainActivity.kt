package top.song_mojing.nest.ui.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.launch
import top.song_mojing.nest.R
import top.song_mojing.nest.manager.ObjectManager
import top.song_mojing.nest.models.Companion
import top.song_mojing.nest.models.Sex
import top.song_mojing.nest.ui.EmbeddedDatePicker
import top.song_mojing.nest.ui.EmbeddedInputBox
import top.song_mojing.nest.ui.EmbeddedSelector
import top.song_mojing.nest.ui.nav.Locale
import top.song_mojing.nest.ui.nav.Settings
import top.song_mojing.nest.ui.nav.Ta
import top.song_mojing.nest.ui.theme.NestTheme
import java.io.File
import java.util.Date


val LocalDarkTheme = compositionLocalOf { false }
val LocalToggleTheme = compositionLocalOf { {} }
val LocalAnimateDurationMillis = compositionLocalOf { 300 }

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val currentSystemTheme = isSystemInDarkTheme()
			var darkTheme by remember { mutableStateOf(currentSystemTheme) }
			val durationMillis = LocalAnimateDurationMillis.current
			val backgroundColor by animateColorAsState(
				targetValue = if (darkTheme) Color(0xFF1C1B1F) else Color(0xFFFFFBFE),
				animationSpec = tween(durationMillis = durationMillis)
			)
			var selectedItem by remember { mutableIntStateOf(0) }
			val items = listOf(Screen.Locale, Screen.Ta, Screen.Settings)
			val view = LocalView.current
			SideEffect {
				val window = (view.context as Activity).window
				WindowInsetsControllerCompat(window, view).apply {
					isAppearanceLightStatusBars = !darkTheme
					isAppearanceLightNavigationBars = !darkTheme
				}
			}
			CompositionLocalProvider(
				LocalAnimateDurationMillis provides durationMillis,
				LocalDarkTheme provides darkTheme,
				LocalToggleTheme provides { darkTheme = !darkTheme }
			) {
				NestTheme(darkTheme = darkTheme) {
					Scaffold(
						modifier = Modifier.fillMaxSize(),
						containerColor = backgroundColor,
						bottomBar = {
							NavigationBar {
								items.forEachIndexed { index, screen ->
									NavigationBarItem(
										selected = selectedItem == index,
										onClick = { selectedItem = index },
										label = { screen.label(selectedItem == index) },
										icon = { screen.icon(selectedItem == index) }
									)
								}
							}
						}
					) { innerPadding ->
						when (selectedItem) {
							0 -> Locale(modifier = Modifier.padding(innerPadding))
							1 -> Ta(modifier = Modifier.padding(innerPadding))
							2 -> Settings(modifier = Modifier.padding(innerPadding))
						}
					}
				}
			}
			LoadConfig()
		}
	}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LoadConfig() {
	if (!ObjectManager.load(configDir = File(LocalContext.current.filesDir, "config"))) {
		val scope = rememberCoroutineScope()
		val sheetState = rememberModalBottomSheetState(
			confirmValueChange = { newValue ->
				newValue != SheetValue.Hidden
			}
		)
		var showBottomSheet by remember { mutableStateOf(true) }
		if (showBottomSheet) {
			ModalBottomSheet(
				onDismissRequest = { showBottomSheet = false },
				sheetState = sheetState,
				containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
				dragHandle = { }
			) {
				BottomSheetContent(
					onClose = {
						scope.launch { sheetState.hide() }.invokeOnCompletion {
							showBottomSheet = false
						}
					},
					create = {
						ObjectManager.self = it
					}
				)
			}
		}
	}
}


@Composable
fun BottomSheetContent(
	onClose: () -> Unit,
	create: (Companion) -> Unit
) {
	var nameText by remember { mutableStateOf(ObjectManager.self?.name ?: "张三") }
	var sexText by remember { mutableStateOf(ObjectManager.self?.sex ?: "男生") }
	var sex by remember { mutableStateOf(Sex.Male) }
	val sexColor = when (sex) {
		Sex.Male -> Color(0xFF1E88E5)
		Sex.Female -> Color(0xFFEA6FBD)
		Sex.Unknown -> Color(0xFF9E9E9E)
	}
	var date: Date? by remember { mutableStateOf(null) }
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 32.dp, vertical = 36.dp)
			.navigationBarsPadding()
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Hi, ",
				style = MaterialTheme.typography.titleLarge
			)
			Spacer(modifier = Modifier.width(5.dp))
			EmbeddedInputBox(
				hint = "你的名字",
				value = nameText,
				onValueChange = { nameText = it }
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "你是",
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.width(5.dp))
			Icon(
				when (sex) {
					Sex.Male -> Icons.Default.Male
					Sex.Female -> Icons.Default.Female
					Sex.Unknown -> Icons.Default.Person
				},
				tint = sexColor,
				contentDescription = null,
				modifier = Modifier.size(20.dp)
			)
			Spacer(modifier = Modifier.width(5.dp))
			EmbeddedSelector(
				hint = "你的性别",
				selectedValue = sexText,
				options = listOf("男生", "女生", "未知"),
				onSelected = {
					sexText = it
					sex = when (it) {
						"男生" -> Sex.Male
						"女生" -> Sex.Female
						else -> Sex.Unknown
					}
				}
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "生于",
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.width(5.dp))
			EmbeddedDatePicker(
				hint = "你的生日",
				selectedDate = date,
				onDateSelected = { date = it }
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			TextButton(
				onClick = onClose,
				modifier = Modifier.weight(1f)
			) {
				Text("关闭")
			}
			Spacer(modifier = Modifier.width(5.dp))
			Button(
				onClick = {
					create(
						Companion(
							name = nameText,
							nickname = null,
							sex = sexText,
							birthday = date
						)
					)
					onClose()
				},
				modifier = Modifier.weight(1f)
			) {
				Text("确定")
			}
		}
	}
}

sealed class Screen(
	val icon: @Composable ((selected: Boolean) -> Unit),
	val label: @Composable ((selected: Boolean) -> Unit)
) {
	object Locale : Screen(
		{
			Icon(Icons.Default.LocationOn, contentDescription = null)
		},
		{
			Text(stringResource(R.string.nav_locale))
		}
	)

	object Ta : Screen(
		{
			val companion = ObjectManager.companion
			if (companion != null) {
				Icon(
					when (it) {
						true -> Icons.Default.Favorite
						else -> Icons.Default.FavoriteBorder
					}, contentDescription = null
				)
			} else {
				Icon(
					when (it) {
						true -> Icons.Default.HeartBroken
						else -> Icons.Default.FavoriteBorder
					}, contentDescription = null
				)
			}
		},
		{
			val companion = ObjectManager.companion
			if (companion != null) {
				Text(companion.nickname ?: companion.name)
			} else {
				Text(stringResource(R.string.nav_ta))
			}
		}
	)

	object Settings : Screen(
		{ selected ->
			val rotation by animateFloatAsState(
				targetValue = if (selected) 90f else 0f,
				animationSpec = tween(durationMillis = LocalAnimateDurationMillis.current / 2)
			)
			Icon(
				imageVector = Icons.Default.Settings,
				contentDescription = null,
				modifier = Modifier.rotate(rotation)
			)
		},
		{
			Text(stringResource(R.string.nav_settings))
		}
	)
}
