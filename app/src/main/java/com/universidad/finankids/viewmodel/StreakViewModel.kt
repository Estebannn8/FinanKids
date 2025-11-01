package com.universidad.finankids.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.UserStreak
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.universidad.finankids.events.AchievementsEventBus
import com.universidad.finankids.events.AchievementTrigger

class StreakViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val _streak = MutableStateFlow(UserStreak())
    val streak: StateFlow<UserStreak> = _streak.asStateFlow()

    fun loadUserStreak(uid: String) {
        if (uid.isEmpty()) return

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("userStreaks").document(uid).get().await()
                val userStreak = snapshot.toObject(UserStreak::class.java)
                    ?: createDefaultStreak(uid)

                _streak.value = userStreak
            } catch (e: Exception) {
                createDefaultStreak(uid)
            }
        }
    }

    private suspend fun createDefaultStreak(uid: String): UserStreak {
        val defaultStreak = UserStreak(uid = uid)
        firestore.collection("userStreaks").document(uid).set(defaultStreak).await()
        _streak.value = defaultStreak
        return defaultStreak
    }

    fun updateStreak(uid: String, lessonCompleted: Boolean = true) {
        if (!lessonCompleted) return

        viewModelScope.launch {
            try {
                val currentStreak = _streak.value
                val today = Date()
                val todayString = dateFormat.format(today)

                // Verificar si ya se registró actividad hoy
                if (currentStreak.streakHistory[todayString] == true) {
                    return@launch
                }

                val calendar = Calendar.getInstance()
                calendar.time = today
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                val yesterdayString = dateFormat.format(calendar.time)

                val wasActiveYesterday = currentStreak.streakHistory[yesterdayString] == true

                val newCurrentStreak = if (wasActiveYesterday) {
                    currentStreak.currentStreak + 1
                } else {
                    // Si no hubo actividad ayer, reiniciar o empezar nueva racha
                    if (currentStreak.lastActivityDate != null) {
                        1 // Reiniciar racha
                    } else {
                        1 // Primera racha
                    }
                }

                val newLongestStreak = maxOf(currentStreak.longestStreak, newCurrentStreak)

                // Actualizar historial
                val updatedHistory = currentStreak.streakHistory.toMutableMap().apply {
                    this[todayString] = true
                }

                val updatedStreak = UserStreak(
                    uid = uid,
                    currentStreak = newCurrentStreak,
                    longestStreak = newLongestStreak,
                    lastActivityDate = today,
                    streakHistory = updatedHistory
                )

                // Guardar en Firestore
                firestore.collection("userStreaks").document(uid).set(updatedStreak).await()
                _streak.value = updatedStreak

                viewModelScope.launch {
                    AchievementsEventBus.emit(
                        AchievementTrigger.StreakChanged(
                            uid = uid,
                            currentStreak = newCurrentStreak
                        )
                    )
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkAndResetStreakIfNeeded(uid: String) {
        viewModelScope.launch {
            try {
                val currentStreak = _streak.value
                val lastActivityDate = currentStreak.lastActivityDate ?: return@launch

                val calendar = Calendar.getInstance()
                val today = calendar.time

                calendar.time = lastActivityDate
                calendar.add(Calendar.DAY_OF_YEAR, 2) // +2 días porque si faltó 1 día se mantiene

                if (today.after(calendar.time)) {
                    // Más de 1 día sin actividad, reiniciar racha
                    val resetStreak = UserStreak(
                        uid = uid,
                        currentStreak = 0,
                        longestStreak = currentStreak.longestStreak,
                        lastActivityDate = currentStreak.lastActivityDate,
                        streakHistory = currentStreak.streakHistory
                    )

                    firestore.collection("userStreaks").document(uid).set(resetStreak).await()
                    _streak.value = resetStreak
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isStreakActive(): Boolean {
        val currentStreak = _streak.value
        val lastActivityDate = currentStreak.lastActivityDate ?: return false

        val calendar = Calendar.getInstance()
        val today = calendar.time

        calendar.time = lastActivityDate
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        return !today.after(calendar.time) // True si la última actividad fue hoy o ayer
    }

    fun clearState() {
        _streak.value = UserStreak()
    }
}