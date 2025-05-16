package com.universidad.finankids.data.model

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject

object JsonUploader {
    private const val TAG = "JsonUploader"

    fun cargarYSubirLecciones(context: Context) {
        val archivos = listOf(
            "ahorro.json" to "ahorro",
            "basica.json" to "basica",
            "compra.json" to "compra",
            "inversion.json" to "inversion"
        )

        val db = FirebaseFirestore.getInstance()
        Log.d(TAG, "Iniciando proceso de carga de lecciones...")

        for ((archivo, categoria) in archivos) {
            try {
                Log.d(TAG, "Procesando archivo: $archivo para categoría: $categoria")

                val jsonString = context.assets.open(archivo).bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonString)
                Log.d(TAG, "Archivo $archivo leído correctamente, ${jsonObject.length()} lecciones encontradas")

                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val leccionId = keys.next()
                    Log.d(TAG, "Procesando lección ID: $leccionId")

                    val leccionData = jsonObject.getJSONObject(leccionId)
                    Log.v(TAG, "Contenido de lección $leccionId: $leccionData")

                    val leccionMap = convertJsonToMap(leccionData)

                    Log.d(TAG, "Subiendo lección $leccionId a Firestore...")
                    db.collection("categorias")
                        .document(categoria)
                        .collection("lecciones")
                        .document(leccionId)
                        .set(leccionMap)
                        .addOnSuccessListener {
                            Log.i(TAG, "✅ Lección $leccionId subida exitosamente a categoría $categoria")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "❌ Error subiendo lección $leccionId a $categoria: ${e.message}")
                        }
                }
            } catch (e: Exception) {
                Log.e(TAG, "⚠️ Error procesando archivo $archivo: ${e.message}")
                e.printStackTrace()
            }
        }
        Log.d(TAG, "Proceso de carga de lecciones completado")
    }

    private fun convertJsonToMap(jsonObject: JSONObject): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        jsonObject.keys().forEach { key ->
            try {
                when (val value = jsonObject.get(key)) {
                    is JSONObject -> map[key] = convertJsonToMap(value)
                    is JSONArray -> map[key] = convertJsonArrayToList(value)
                    else -> map[key] = value
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando key $key: ${e.message}")
            }
        }
        return map
    }

    private fun convertJsonArrayToList(jsonArray: JSONArray): List<Any> {
        val list = mutableListOf<Any>()
        for (i in 0 until jsonArray.length()) {
            try {
                when (val value = jsonArray.get(i)) {
                    is JSONObject -> list.add(convertJsonToMap(value))
                    is JSONArray -> list.add(convertJsonArrayToList(value))
                    else -> list.add(value)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando elemento $i del array: ${e.message}")
            }
        }
        return list
    }
}