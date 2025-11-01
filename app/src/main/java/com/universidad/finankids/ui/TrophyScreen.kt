package com.universidad.finankids.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.universidad.finankids.R
import com.universidad.finankids.data.model.LogroUI
import com.universidad.finankids.navigation.navigateToScreen
import com.universidad.finankids.ui.components.AchievementDetailDialog
import com.universidad.finankids.ui.components.BottomMenu
import com.universidad.finankids.viewmodel.AchievementsViewModel
import com.universidad.finankids.viewmodel.UserViewModel

@Composable
fun TrophyScreen(
    navController: NavController,
    achievementsViewModel: AchievementsViewModel,
    userViewModel: UserViewModel
) {
    var selectedItem = remember { mutableStateOf("home") }
    val achievementsState by achievementsViewModel.state.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val achievements = achievementsState.uiAchievements

    val uid = userState.userData.uid

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) achievementsViewModel.load(uid)
    }

    var selectedAchievement by remember { mutableStateOf<LogroUI?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1CF86)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Text(
            text = "MIS LOGROS",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.baloo_regular)),
                color = Color.Black,
                letterSpacing = 3.sp
            )
        )

        Spacer(modifier = Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_pesito_logros),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(top = 8.dp)
                    )

                    achievements.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            row.forEach { logroUI ->
                                AchievementItem(
                                    logroUI = logroUI,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                ) {
                                    selectedAchievement = logroUI
                                }
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

    // 👇 Mostrar detalle
    if (selectedAchievement != null) {
        Dialog(onDismissRequest = { selectedAchievement = null }) {
            AchievementDetailDialog(
                logroUI = selectedAchievement!!,
                onClaim = {
                    achievementsViewModel.claimReward(uid, selectedAchievement!!.logro.id)
                },
                onDismiss = { selectedAchievement = null }
            )
        }
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
fun AchievementItem(
    logroUI: LogroUI,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val grayscale = !logroUI.desbloqueado
    val rewardPending = logroUI.desbloqueado && !logroUI.reclamado

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(logroUI.logro.iconoUrl),
            contentDescription = logroUI.logro.titulo,
            modifier = Modifier.fillMaxSize(),
            colorFilter = if (grayscale) {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            } else null,
            contentScale = ContentScale.Crop
        )

        if (rewardPending) {
            Image(
                painter = painterResource(id = R.drawable.multiple_coins),
                contentDescription = "Recompensa pendiente",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(28.dp)
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun TrophyScreenPreview() {
    val navController = rememberNavController()
    TrophyScreen(navController = navController)
}
 */
