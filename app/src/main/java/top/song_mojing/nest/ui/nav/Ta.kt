package top.song_mojing.nest.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.song_mojing.nest.manager.ObjectManager
import top.song_mojing.nest.models.Companion

@Composable
fun Ta(
	modifier: Modifier = Modifier
) {
	Box(modifier = modifier) {
		val companion = ObjectManager.companion
		if (companion != null) {
			Companion(companion = companion)
		} else {
			Nobody()
		}
	}
}

@Composable
private fun Companion(
	companion: Companion
) {
	companion.nickname?.let { Text(text = it) }
}

@Composable
private fun Nobody() {
	Text(text = "没有人")
}