package top.song_mojing.nest.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.launch
import top.song_mojing.nest.R
import top.song_mojing.nest.manager.ObjectManager
import top.song_mojing.nest.ui.nav.Locale
import top.song_mojing.nest.ui.nav.Settings
import top.song_mojing.nest.ui.nav.Ta
import top.song_mojing.nest.ui.theme.NestTheme
import top.song_mojing.nest.ui.window.PersonalInfo
import java.io.File


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
		val sheetState = rememberModalBottomSheetState()
		var showBottomSheet by remember { mutableStateOf(true) }
		LaunchedEffect(sheetState.isVisible) {
			if (!sheetState.isVisible) {
				showBottomSheet = false
			}
		}
		if (showBottomSheet) {
			ModalBottomSheet(
				onDismissRequest = { scope.launch { sheetState.hide() } },
				sheetState = sheetState,
				containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
				dragHandle = { }
			) {
				PersonalInfo(
					onClose = {
						scope.launch { sheetState.hide() }
					},
					onConfirm = {
						ObjectManager.self = it
						scope.launch { sheetState.hide() }
					}
				)
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
