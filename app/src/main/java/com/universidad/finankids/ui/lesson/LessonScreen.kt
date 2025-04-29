package com.universidad.finankids.ui.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.universidad.finankids.R
import com.universidad.finankids.ui.Components.CustomButton
import com.universidad.finankids.ui.lesson.activities.DragPairsActivity
import com.universidad.finankids.ui.lesson.activities.FillBlankActivity
import com.universidad.finankids.ui.lesson.activities.MatchingActivity
import com.universidad.finankids.ui.lesson.activities.MultipleChoiceActivity
import com.universidad.finankids.ui.lesson.activities.SentenceBuilderActivity

@Composable
fun LessonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
                                // Agregar fondo Dinamico
    ) {
        // Header que contiene la flecha para atrás, barra de progreso y vidas
        LessonHeader()

        // Aquí vendría el contenido de la lección (por ejemplo, actividades específicas)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            // Contenido de la lección
            DragPairsActivity()
        }

        // Bottom Section
        BottomSection()

    }
}

@Composable
fun LessonHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ) {

        // Flecha de retroceso
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 10.dp, start = 10.dp)
                .align(Alignment.CenterStart)
                .clickable { }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_atras_recovery), // Dinamico
                contentDescription = "Flecha atras",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Barra de progreso y vidas
        Box (
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(32.dp)
                .align(Alignment.Center)
        ) {
            // Fondo de la barra
            Image(
                painter = painterResource(R.drawable.ic_exp_bar_morado), // Dinamico
                contentDescription = "Barra de progreso",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Progreso
            Box(
                modifier = Modifier
                    .fillMaxWidth(0f) // Progreso
                    .height(17.dp)
                    .padding(end = 12.2.dp)
                    .zIndex(3f)
                    .offset(y = (3.73).dp, x = 5.8.dp)
                    .clip(RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    ))
                    .background(Color.Magenta) // Dinamico
            )
        }

        // --- Vidas ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(y = (-3).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_full_heart),
                contentDescription = "Vida",
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "1", // Vidas actuales
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

    }
}

@Composable
fun BottomSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(80.dp)
    ){
        CustomButton(
            modifier = Modifier
                .align(Alignment.Center),
            buttonText = "CONTINUAR",
            gradientLight = Color(0xFF9C749A),  // Dinamico
            gradientDark = Color(0xFF431441),  // Dinamico
            baseColor = Color(0xFF53164F),  // Dinamico
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLessonScreen() {
    LessonScreen()
}
