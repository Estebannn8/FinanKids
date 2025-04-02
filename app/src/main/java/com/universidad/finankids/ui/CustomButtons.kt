package com.universidad.finankids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .width(287.dp)
            .height(37.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF9C749A),
                        Color(0xFF421440),
                    ),
                    start = Offset.Zero,
                    end = Offset(0F,36.720542907714844F),
                ),
                shape = RoundedCornerShape(40.879302978515625.dp)
            )
            .border(
                width = 3.dp,
                color = Color(0xFF53164F),
                shape = RoundedCornerShape(40.879302978515625.dp)
            )
    ){
        Column(
            modifier = Modifier
                .offset(x = 0.dp, y = 5.05078.dp)
                .padding(2.06595.dp)
                .width(287.dp)
                .height(23.0645.dp)
                .background(
                    color = Color(0xFF53164F),
                    shape = RoundedCornerShape(size = 40.8793.dp)
                )
        ){
            Row(
                modifier = Modifier
                    .offset(x = 73.dp, y = 0.dp)
                    .width(140.dp)
                    .height(19.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "RESTABLECER ",
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}

@Preview(widthDp = 287, heightDp = 36)
@Composable
private fun BtnMoradoPreview() {
    CustomButton(Modifier)
}

