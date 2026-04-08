package top.song_mojing.nest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.song_mojing.nest.ui.theme.NestTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			var darkTheme by remember { mutableStateOf(false) }
			
			// 背景渐变颜色动画
			val backgroundColor by animateColorAsState(
				targetValue = if (darkTheme) Color(0xFF1C1B1F) else Color(0xFFFFFBFE),
				animationSpec = tween(durationMillis = 300),
				label = "backgroundColor"
			)
			
			// 同步更新系统状态栏和导航栏颜色
			SideEffect {
				if (darkTheme) {
					enableEdgeToEdge(
						statusBarStyle = SystemBarStyle.dark(Color(0xFF1C1B1F).toArgb()),
						navigationBarStyle = SystemBarStyle.dark(Color(0xFF1C1B1F).toArgb())
					)
				} else {
					enableEdgeToEdge(
						statusBarStyle = SystemBarStyle.light(Color(0xFFFFFBFE).toArgb(), Color(0xFFFFFBFE).toArgb()),
						navigationBarStyle = SystemBarStyle.light(Color(0xFFFFFBFE).toArgb(), Color(0xFFFFFBFE).toArgb())
					)
				}
			}
			
			NestTheme(darkTheme = darkTheme) {
				Scaffold(
					modifier = Modifier.fillMaxSize(),
					containerColor = backgroundColor
				) { innerPadding ->
					Greeting(
						name = "Android",
						darkTheme = darkTheme,
						onToggleTheme = { darkTheme = !darkTheme },
						modifier = Modifier.padding(innerPadding)
					)
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, darkTheme: Boolean, onToggleTheme: () -> Unit, modifier: Modifier = Modifier) {
	// 文本颜色动画
	val textColor by animateColorAsState(
		targetValue = if (darkTheme) Color.White else Color.Black,
		animationSpec = tween(durationMillis = 300),
		label = "textColor"
	)
	
	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "Hello $name!",
			color = textColor,
			modifier = Modifier.padding(bottom = 16.dp)
		)
		Button(
			onClick = onToggleTheme
		) {
			Text(
				text = if (darkTheme) "切换到亮色主题" else "切换到暗色主题"
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	NestTheme {
		Greeting("Android", darkTheme = false, onToggleTheme = {})
	}
}