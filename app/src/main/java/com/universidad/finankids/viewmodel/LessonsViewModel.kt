package com.universidad.finankids.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.universidad.finankids.data.model.Lesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Versión optimizada del ViewModel
class LessonsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadLessonsForCategory(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true  // <--- INICIA CARGA
            _lessons.value = emptyList()
            try {
                val snapshot = firestore.collection("categorias/$categoryId/lecciones")
                    .orderBy("order")
                    .get()
                    .await()

                snapshot.documents.forEach { doc ->
                    Log.d("FirestoreData", "Doc ID: ${doc.id}, Data: ${doc.data}")
                }

                _lessons.value = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Lesson::class.java)?.copy(id = doc.id).also {
                            Log.d("MappedLesson", "Lección mapeada: ${it?.id}")
                        }
                    } catch (e: Exception) {
                        Log.e("MappingError", "Error al mapear lección ${doc.id}: ${e.message}")
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error: ${e.message}")
            } finally {
                _isLoading.value = false  // <--- TERMINA CARGA
            }
        }
    }


    fun getNextUncompletedLesson(completedLessons: Map<String, Any>): Lesson? {
        return lessons.value
            .sortedBy { it.order }
            .firstOrNull { lesson ->
                !completedLessons.any { it.key == lesson.id }
            }
            ?.also { Log.d("NextLesson", "Encontrada lección no completada: ${it.id}") }
    }
}