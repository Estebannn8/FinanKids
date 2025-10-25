package com.universidad.finankids.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.universidad.finankids.R
import com.universidad.finankids.data.model.UserStreak
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun StreakCalendarOverlay(
    streak: UserStreak,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "TU RACHA DE APRENDIZAJE",
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Estadísticas de racha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Racha Actual",
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                        Text(
                            text = "${streak.currentStreak} días",
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Mejor Racha",
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                        Text(
                            text = "${streak.longestStreak} días",
                            fontFamily = FontFamily(Font(R.font.baloo_regular)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Calendario
                StreakCalendar(streak = streak)

                Spacer(modifier = Modifier.height(16.dp))

                // Leyenda
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem(
                        iconRes = R.drawable.ic_racha_normal,
                        text = "Día completado"
                    )
                    LegendItem(
                        iconRes = R.drawable.ic_racha_apagada,
                        text = "Día perdido"
                    )
                }
            }
        }
    }
}

@Composable
fun StreakCalendar(streak: UserStreak) {
    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)

    LazyColumn {
        items(months) { month ->
            MonthCalendar(
                monthName = month,
                year = currentYear,
                monthIndex = months.indexOf(month),
                streakHistory = streak.streakHistory
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MonthCalendar(monthName: String, year: Int, monthIndex: Int, streakHistory: Map<String, Boolean>) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance().apply {
        set(year, monthIndex, 1)
    }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    Column {
        // Header del mes
        Text(
            text = "$monthName $year",
            fontFamily = FontFamily(Font(R.font.baloo_regular)),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Días del mes
        var dayCounter = 1
        val weeks = mutableListOf<List<Int?>>()
        var currentWeek = mutableListOf<Int?>()

        // Espacios vacíos para el primer día
        repeat(firstDayOfWeek - 2) { // -2 porque Calendar.DAY_OF_WEEK empieza en Domingo=1
            currentWeek.add(null)
        }

        while (dayCounter <= daysInMonth) {
            if (currentWeek.size == 7) {
                weeks.add(currentWeek)
                currentWeek = mutableListOf()
            }
            currentWeek.add(dayCounter)
            dayCounter++
        }

        // Completar la última semana
        while (currentWeek.size < 7) {
            currentWeek.add(null)
        }
        weeks.add(currentWeek)

        // Renderizar semanas
        Column {
            weeks.forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { day ->
                        DayCell(
                            day = day,
                            month = monthIndex + 1,
                            year = year,
                            streakHistory = streakHistory,
                            dateFormat = dateFormat
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun DayCell(day: Int?, month: Int, year: Int, streakHistory: Map<String, Boolean>, dateFormat: SimpleDateFormat) {
    val isActive = if (day != null) {
        val dateString = String.format("%04d-%02d-%02d", year, month, day)
        streakHistory[dateString] == true
    } else {
        false
    }

    Box(
        modifier = Modifier
            .size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        if (day != null) {
            if (isActive) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_racha_normal),
                    contentDescription = "Día activo",
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(
                    text = day.toString(),
                    fontFamily = FontFamily(Font(R.font.baloo_regular)),
                    fontSize = 12.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }
    }
}

@Composable
fun LegendItem(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.baloo_regular)),
            fontSize = 12.sp,
            color = Color(0xFF666666)
        )
    }
}