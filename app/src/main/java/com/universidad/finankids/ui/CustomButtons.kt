package com.universidad.finankids.ui

import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em


@Composable
fun BtnGris() {
    Box(
        modifier = Modifier
            .width(287.dp)
            .height(36.28.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.Gray)
            .padding(start = 0.dp, top = 5.dp)
    ) {
        // Rectangle 8
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.72.dp)
                .padding(start = 0.dp, top = 0.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.Gray)
        )

        // Frame 28 with text
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(0.dp, 1.dp)
        ) {
            Text(
                text = "RESTABLECER",
                fontSize = 16.88.sp,
                letterSpacing = 3.07.sp,
                lineHeight = 1.12.em,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        // Group 2 (Ellipses)
        Row(
            modifier = Modifier
                .padding(start = 11.dp, top = 3.dp),
            horizontalArrangement = Arrangement.Start // Esto alinea a la izquierda
        ) {
            // Ellipse 22
            Box(
                modifier = Modifier
                    .size(17.2.dp, 7.9.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            // Ellipse 23
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(5.48.dp, 5.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}


@Preview
@Composable
fun ButtonPreview(){

}
