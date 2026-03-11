package com.klen0010.flinders.zootreasurehunt

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private enum class DragAnchors {
    START,
    END,
}

@Composable
fun SwipeableSighting(
    sighting: Sighting,
    onEditClick: (Sighting) -> Unit,
    onSwipe: () -> Unit = {}
) {
    val dragState = remember {
        AnchoredDraggableState(initialValue = DragAnchors.START)
    }

    LaunchedEffect(dragState.settledValue) {
        if (dragState.settledValue == DragAnchors.END){
            onSwipe()
        }
    }

    val cardShape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.width.toFloat()
                dragState.updateAnchors(
                    DraggableAnchors {
                        DragAnchors.START at 0f
                        DragAnchors.END at dragEndPoint
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Red),
                contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = "Delete",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Card(
            modifier = Modifier
            .offset {
            IntOffset(
                x = dragState.requireOffset().roundToInt(),
                y = 0
            )
        }
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Horizontal
            ),
            shape = cardShape
        ){
            AnimalCard(
                sighting = sighting,
                onClick = { onEditClick(sighting) }
            )
        }
    }
}