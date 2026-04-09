package top.song_mojing.nest.ui.activity.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.song_mojing.nest.ui.theme.NestTheme

class PermissionsSettings : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			NestTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Greeting(
						name = "Android",
						modifier = Modifier.padding(innerPadding)
					)
				}
			}
		}
	}
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}