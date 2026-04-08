package top.song_mojing.nest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EmbeddedInputBox(
	hint: String,
	value: String,
	onValueChange: (String) -> Unit,
	textColor: Color = MaterialTheme.colorScheme.onSurface
) {
	BasicTextField(
		value = value,
		onValueChange = onValueChange,
		modifier = Modifier
			.drawBehind {
				val strokeWidth = 2f
				val y = size.height - strokeWidth
				drawLine(
					color = textColor.copy(alpha = 0.5f),
					start = Offset(0f, y),
					end = Offset(size.width, y),
					strokeWidth = strokeWidth
				)
			},
		textStyle = MaterialTheme.typography.bodyLarge.copy(
			color = textColor,
			textAlign = TextAlign.Start
		),
		singleLine = true,
		cursorBrush = SolidColor(textColor),
		decorationBox = { innerTextField ->
			Box(
				contentAlignment = Alignment.CenterStart,
				modifier = Modifier.padding(bottom = 4.dp)
			) {
				if (value.isEmpty()) {
					Text(
						text = hint,
						style = MaterialTheme.typography.bodyLarge.copy(
							color = textColor.copy(alpha = 0.5f),
							textAlign = TextAlign.Start
						),
					)
				}
				innerTextField()
			}
		}
	)
}

@Composable
fun EmbeddedSelector(
	hint: String,
	selectedValue: String,
	options: List<String>,
	onSelected: (String) -> Unit,
	textColor: Color = MaterialTheme.colorScheme.onSurface
) {
	var expanded by remember { mutableStateOf(false) }
	Box(
		modifier = Modifier
			.drawBehind {
				val strokeWidth = 2f
				val y = size.height - strokeWidth
				drawLine(
					color = textColor.copy(alpha = 0.5f),
					start = Offset(0f, y),
					end = Offset(size.width, y),
					strokeWidth = strokeWidth
				)
			}
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null
			) {
				expanded = true
			}
			.padding(bottom = 4.dp)
			.padding(horizontal = 4.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.widthIn(min = 40.dp)
		) {
			Text(
				text = selectedValue.ifEmpty { hint },
				style = MaterialTheme.typography.bodyLarge.copy(
					color = if (selectedValue.isEmpty()) textColor.copy(alpha = 0.5f) else textColor,
					textAlign = TextAlign.Start
				)
			)
			Icon(
				imageVector = Icons.Default.ArrowDropDown,
				contentDescription = null,
				tint = textColor.copy(alpha = 0.4f),
				modifier = Modifier.size(20.dp)
			)
		}
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false },
			modifier = Modifier.background(MaterialTheme.colorScheme.surface)
		) {
			options.forEach { option ->
				DropdownMenuItem(
					text = { Text(option) },
					onClick = {
						onSelected(option)
						expanded = false
					}
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmbeddedDatePicker(
	hint: String,
	selectedDate: Date?,
	onDateSelected: (Date) -> Unit,
	formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
	textColor: Color = MaterialTheme.colorScheme.onSurface
) {
	var showSheet by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val scope = rememberCoroutineScope()
	Box(
		modifier = Modifier
			.drawBehind {
				val strokeWidth = 2f
				val y = size.height - strokeWidth
				drawLine(
					color = textColor.copy(alpha = 0.5f),
					start = Offset(0f, y),
					end = Offset(size.width, y),
					strokeWidth = strokeWidth
				)
			}
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null
			) {
				showSheet = true
			}
			.padding(bottom = 4.dp)
			.padding(horizontal = 4.dp)
	) {
		Text(
			text = selectedDate?.let { formatter.format(it) } ?: hint,
			style = MaterialTheme.typography.bodyLarge.copy(
				color = if (selectedDate == null) textColor.copy(alpha = 0.5f) else textColor
			),
			modifier = Modifier.padding(end = 8.dp)
		)
	}
	if (showSheet) {
		val datePickerState = rememberDatePickerState(
			initialSelectedDateMillis = selectedDate?.time,
			initialDisplayMode = DisplayMode.Picker
		)
		ModalBottomSheet(
			onDismissRequest = { showSheet = false },
			sheetState = sheetState,
			containerColor = MaterialTheme.colorScheme.surface,
			dragHandle = null,
			scrimColor = Black.copy(alpha = 0.32f)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 32.dp)
			) {
				DatePicker(
					state = datePickerState,
					showModeToggle = false,
					title = null,
					headline = null,
					// 强制让内部背景透明
					colors = DatePickerDefaults.colors().copy(
						containerColor = MaterialTheme.colorScheme.surface,
						dividerColor = Color.Transparent
					)
				)
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 24.dp),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.CenterVertically
				) {
					TextButton(onClick = {
						scope.launch { sheetState.hide() }.invokeOnCompletion {
							showSheet = false
						}
					}) {
						Text("取消")
					}
					Button(
						onClick = {
							datePickerState.selectedDateMillis?.let {
								onDateSelected(Date(it))
							}
							scope.launch { sheetState.hide() }.invokeOnCompletion {
								showSheet = false
							}
						},
						modifier = Modifier.padding(start = 8.dp)
					) {
						Text("确定")
					}
				}
			}
		}
	}
}