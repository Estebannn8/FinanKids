package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.universidad.finankids.R
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.BottomMenu

@Composable
fun TrophyScreen(navController: NavController) {
    var selectedItem = remember { mutableStateOf("home") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1CF86)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mis Logros",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, bottom = 32.dp),

            textAlign = TextAlign.Center
        )


        Spacer(modifier = Modifier.weight(1f))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp)
                .padding(horizontal = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE1E1E1))
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(120.dp)) // espacio para muñeco

                    repeat(6) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            repeat(3) {
                                AchievementItem(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 24.dp, y = (-20).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8C856))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "LOGROS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomMenu(
            isHomeSection = false,
            sectionColor = "",
            menuBackgroundColor = Color(0xFFF0F3F6),
            selectedItem = selectedItem.value,
            onItemSelected = { item ->
                selectedItem.value = item
                navigateToScreen(navController, item)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun AchievementsContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE6E6E6))
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 36.dp, bottom = 20.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun AchievementItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.achievement_placeholder),
            contentDescription = "Achievement",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrophyScreenPreview() {
    val navController = rememberNavController()
    TrophyScreen(navController = navController)
}
