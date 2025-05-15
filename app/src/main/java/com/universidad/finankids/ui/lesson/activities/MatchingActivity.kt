package com.universidad.finankids.ui.lesson.activities

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universidad.finankids.data.model.MatchingPair
import com.universidad.finankids.events.LessonEvent
import com.universidad.finankids.state.LessonState
import com.universidad.finankids.ui.lesson.TemaVisual
import kotlinx.coroutines.delay

@Composable
fun MatchingActivity(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit,
    temaVisual: TemaVisual
) {
    val activity = state.currentActivity ?: return

    val leftItems = remember { (activity.matchingPairs?.map { it.leftItem } ?: emptyList()).shuffled() }
    val rightItems = remember {
        activity.shuffledRightItems ?: (activity.matchingPairs?.map { it.rightItem } ?: emptyList()).shuffled()
    }

    val pastelColors = remember {
        listOf(
            Color(0xDFB39DDB).copy(alpha = 0.7f), Color(0xDF81C784).copy(alpha = 0.7f),
            Color(0xDF64B5F6).copy(alpha = 0.7f), Color(0xDFFFB74D).copy(alpha = 0.7f),
            Color(0xDFE57373).copy(alpha = 0.7f), Color(0xDF9575CD).copy(alpha = 0.7f),
            Color(0xDF4DB6AC).copy(alpha = 0.7f), Color(0xDFFFD54F).copy(alpha = 0.7f)
        )
    }

    val pairColors = remember { mutableStateOf(mapOf<MatchingPair, Color>()) }
    val leftItemBounds = remember { mutableStateMapOf<String, Rect>() }
    val rightItemBounds = remember { mutableStateMapOf<String, Rect>() }

    class MatchedLine(val start: Offset, val end: Offset, val color: Color)
    val matchedLines = remember { mutableStateListOf<MatchedLine>() }

    val isDragging = remember { mutableStateOf(false) }
    val dragStartPosition = remember { mutableStateOf<Offset?>(null) }
    val dragCurrentPosition = remember { mutableStateOf<Offset?>(null) }
    val dragStartItem = remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    val dragEndItem = remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    val rootCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }

    fun Rect.center() = Offset(left + (right - left) / 2, top + (bottom - top) / 2)

    fun Rect.containsWithPadding(point: Offset, padding: Float = 20f): Boolean {
        val paddedRect = Rect(left - padding, top - padding, right + padding, bottom + padding)
        return paddedRect.contains(point)
    }

    fun findItemAtPosition(position: Offset): Pair<String, Boolean>? {
        for ((item, bounds) in leftItemBounds) {
            if (bounds.containsWithPadding(position)) return Pair(item, true)
        }
        for ((item, bounds) in rightItemBounds) {
            if (bounds.containsWithPadding(position)) return Pair(item, false)
        }
        return null
    }

    fun isItemMatched(item: String, isLeft: Boolean): Boolean {
        return if (isLeft) {
            state.matchedPairs.any { it.leftItem == item }
        } else {
            state.matchedPairs.any { it.rightItem == item }
        }
    }

    fun Offset.toCanvasLocal(): Offset {
        val root = rootCoordinates.value ?: return this
        val canvasOffset = root.positionInRoot()
        return this - canvasOffset
    }

    fun logItemBounds() {
        Log.d("BOUNDS", "=== ITEM BOUNDS ===")
        leftItemBounds.forEach { (item, rect) ->
            Log.d("BOUNDS", "LEFT '$item': (${rect.left}, ${rect.top}) - (${rect.right}, ${rect.bottom})")
        }
        rightItemBounds.forEach { (item, rect) ->
            Log.d("BOUNDS", "RIGHT '$item': (${rect.left}, ${rect.top}) - (${rect.right}, ${rect.bottom})")
        }
    }

    LaunchedEffect(state.matchedPairs) {
        matchedLines.clear()
        state.matchedPairs.forEach { pair ->
            val leftBounds = leftItemBounds[pair.leftItem]
            val rightBounds = rightItemBounds[pair.rightItem]
            if (leftBounds != null && rightBounds != null) {
                val color = pairColors.value[pair] ?: Color.Gray
                matchedLines.add(
                    MatchedLine(
                        leftBounds.center().toCanvasLocal(),
                        rightBounds.center().toCanvasLocal(),
                        color
                    )
                )
            }
        }
    }

    fun tryMatchingPair(left: String, right: String) {
        // Validar que ninguno de los items ya esté emparejado
        val isLeftAlreadyMatched = state.matchedPairs.any { it.leftItem == left }
        val isRightAlreadyMatched = state.matchedPairs.any { it.rightItem == right }

        if (!isLeftAlreadyMatched && !isRightAlreadyMatched) {
            val newPair = MatchingPair(left, right)
            onEvent(LessonEvent.MatchPair(newPair))

            if (newPair !in pairColors.value) {
                val availableColors = pastelColors.filter { it !in pairColors.value.values }
                val randomColor = availableColors.randomOrNull() ?: pastelColors.random()
                pairColors.value = pairColors.value + (newPair to randomColor)
            }

            val leftBounds = leftItemBounds[left]
            val rightBounds = rightItemBounds[right]
            if (leftBounds != null && rightBounds != null) {
                val color = pairColors.value[newPair] ?: Color.Gray
                matchedLines.add(
                    MatchedLine(
                        leftBounds.center().toCanvasLocal(),
                        rightBounds.center().toCanvasLocal(),
                        color
                    )
                )
            }

            onEvent(LessonEvent.SelectLeftItem(null))
            onEvent(LessonEvent.SelectRightItem(null))
        } else {
            // Mostrar feedback al usuario que los items ya están emparejados
            // Puedes implementar un mensaje visual aquí si lo deseas
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        logItemBounds()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            Text(
                text = activity.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates -> rootCoordinates.value = coordinates }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { localOffset ->
                                val offset = rootCoordinates.value?.localToRoot(localOffset) ?: localOffset
                                val itemAtPosition = findItemAtPosition(offset)
                                dragStartItem.value = itemAtPosition
                                dragEndItem.value = null

                                if (itemAtPosition != null) {
                                    val (item, isLeft) = itemAtPosition
                                    if (!isItemMatched(item, isLeft)) {
                                        val bounds = if (isLeft) leftItemBounds[item] else rightItemBounds[item]
                                        dragStartPosition.value = (bounds?.center() ?: offset).toCanvasLocal()
                                        dragCurrentPosition.value = dragStartPosition.value
                                    }
                                } else {
                                    dragStartPosition.value = offset.toCanvasLocal()
                                    dragCurrentPosition.value = offset.toCanvasLocal()
                                }
                                isDragging.value = true
                            },
                            onDrag = { change, _ ->
                                val offset = rootCoordinates.value?.localToRoot(change.position) ?: change.position
                                dragCurrentPosition.value = offset.toCanvasLocal()
                                val currentItem = findItemAtPosition(offset)
                                if (currentItem != dragEndItem.value) dragEndItem.value = currentItem
                            },
                            onDragEnd = {
                                if (isDragging.value) {
                                    if (dragStartItem.value != null && dragEndItem.value != null) {
                                        val (startItem, startIsLeft) = dragStartItem.value!!
                                        val (endItem, endIsLeft) = dragEndItem.value!!
                                        if (startIsLeft != endIsLeft) {
                                            if (!isItemMatched(startItem, startIsLeft) &&
                                                !isItemMatched(endItem, endIsLeft)
                                            ) {
                                                if (startIsLeft) {
                                                    tryMatchingPair(startItem, endItem)
                                                } else {
                                                    tryMatchingPair(endItem, startItem)
                                                }
                                            }
                                        }
                                    }
                                    isDragging.value = false
                                    dragStartPosition.value = null
                                    dragCurrentPosition.value = null
                                    dragStartItem.value = null
                                    dragEndItem.value = null
                                }
                            }
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    matchedLines.forEach { line ->
                        drawLine(line.color, line.start, line.end, strokeWidth = 6f, cap = StrokeCap.Round)
                    }
                    if (isDragging.value && dragStartPosition.value != null && dragCurrentPosition.value != null) {
                        drawLine(
                            color = Color.Gray,
                            start = dragStartPosition.value!!,
                            end = dragCurrentPosition.value!!,
                            strokeWidth = 4f,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        leftItems.forEach { item ->
                            val matchedPair = state.matchedPairs.find { it.leftItem == item }
                            MatchingItemSimple(
                                text = item,
                                isSelected = state.selectedLeft == item,
                                isMatched = matchedPair != null,
                                pairColor = pairColors.value[matchedPair],
                                onClick = {
                                    matchedPair?.let {
                                        onEvent(LessonEvent.UnmatchPair(it))
                                    } ?: run {
                                        val newLeft = if (state.selectedLeft == item) null else item
                                        onEvent(LessonEvent.SelectLeftItem(newLeft))
                                        if (newLeft != null && state.selectedRight != null) {
                                            tryMatchingPair(newLeft, state.selectedRight)
                                        }
                                    }
                                },
                                onBoundsChange = { bounds -> leftItemBounds[item] = bounds }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        rightItems.forEach { item ->
                            val matchedPair = state.matchedPairs.find { it.rightItem == item }
                            MatchingItemSimple(
                                text = item,
                                isSelected = state.selectedRight == item,
                                isMatched = matchedPair != null,
                                pairColor = pairColors.value[matchedPair],
                                onClick = {
                                    matchedPair?.let {
                                        onEvent(LessonEvent.UnmatchPair(it))
                                    } ?: run {
                                        val newRight = if (state.selectedRight == item) null else item
                                        onEvent(LessonEvent.SelectRightItem(newRight))
                                        if (newRight != null && state.selectedLeft != null) {
                                            tryMatchingPair(state.selectedLeft, newRight)
                                        }
                                    }
                                },
                                onBoundsChange = { bounds -> rightItemBounds[item] = bounds }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MatchingItemSimple(
    text: String,
    isSelected: Boolean,
    isMatched: Boolean,
    pairColor: Color?,
    onClick: () -> Unit,
    onBoundsChange: (androidx.compose.ui.geometry.Rect) -> Unit
) {
    val backgroundColor = when {
        isMatched -> pairColor ?: Color(0xFF4CAF50)
        else -> Color.White.copy(alpha = 0.7f)
    }
    val textColor = if (isMatched) Color.White else Color.Black
    val borderColor = when {
        isSelected -> Color(0xFF1976D2)
        isMatched -> Color.Black.copy(alpha = 0.5f)
        else -> Color(0xFFBBBBBB)
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clickable { onClick() }
            .onGloballyPositioned { coordinates ->
                val rootOffset = coordinates.localToRoot(Offset.Zero)
                val size = coordinates.size
                val rect = androidx.compose.ui.geometry.Rect(
                    rootOffset.x,
                    rootOffset.y,
                    rootOffset.x + size.width,
                    rootOffset.y + size.height
                )
                onBoundsChange(rect)
            },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(borderWidth, borderColor),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
