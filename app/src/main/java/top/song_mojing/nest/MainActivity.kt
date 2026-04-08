package top.song_mojing.nest

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import top.song_mojing.nest.ui.theme.NestTheme


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
						containerColor = backgroundColor
					) { innerPadding ->
						Index(
							modifier = Modifier.padding(innerPadding)
						)
					}
				}
			}
		}
	}
}

@Composable
fun Index(modifier: Modifier = Modifier) {
	val isDarkTheme = LocalDarkTheme.current
	val toggleTheme = LocalToggleTheme.current

	val textColor by animateColorAsState(
		targetValue = if (isDarkTheme) Color.White else Color.Black,
		animationSpec = tween(durationMillis = LocalAnimateDurationMillis.current),
		label = "textColor"
	)

	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "Hello World!",
			color = textColor,
			modifier = Modifier.padding(bottom = 16.dp)
		)
		Button(
			onClick = toggleTheme
		) {
			Text(
				text = if (isDarkTheme) "切换到亮色主题" else "切换到暗色主题"
			)
		}
	}
}