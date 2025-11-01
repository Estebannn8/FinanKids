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

    fun cargarYSubirLogros(context: Context) {
        val db = FirebaseFirestore.getInstance()
        val archivo = "logros.json"

        Log.d(TAG, "Iniciando proceso de carga de logros...")

        try {
            val jsonString = context.assets.open(archivo).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            Log.d(TAG, "Archivo $archivo leído correctamente, ${jsonArray.length()} logros encontrados")

            Log.d(TAG, "Contenido completo JSON: $jsonString")

            for (i in 0 until jsonArray.length()) {
                val logroData = jsonArray.getJSONObject(i)
                val logroId = logroData.getString("id")

                val logroMap = convertJsonToMap(logroData)

                Log.d(TAG, "Subiendo logro $logroId a Firestore...")
                db.collection("logros")
                    .document(logroId)
                    .set(logroMap)
                    .addOnSuccessListener {
                        Log.i(TAG, "✅ Logro $logroId subido exitosamente")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "❌ Error subiendo logro $logroId: ${e.message}")
                    }
            }

        } catch (e: Exception) {
            Log.e(TAG, "⚠️ Error procesando archivo $archivo: ${e.message}")
            e.printStackTrace()
        }

        Log.d(TAG, "Proceso de carga de logros completado")
    }


    fun convertJsonToMap(json: JSONObject): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = json.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            val value = json.get(key)

            map[key] = when (value) {
                JSONObject.NULL -> null
                is JSONObject -> convertJsonToMap(value)
                is JSONArray -> convertJsonArray(value)
                else -> value
            }
        }

        return map
    }

    fun convertJsonArray(array: JSONArray): List<Any?> {
        val list = mutableListOf<Any?>()

        for (i in 0 until array.length()) {
            val value = array.get(i)
            list.add(
                when (value) {
                    JSONObject.NULL -> null
                    is JSONObject -> convertJsonToMap(value)
                    is JSONArray -> convertJsonArray(value)
                    else -> value
                }
            )
        }

        return list
    }

}