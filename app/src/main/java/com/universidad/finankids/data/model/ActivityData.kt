package com.universidad.finankids.data.model

sealed class ActivityType {
    object Teaching : ActivityType()
    object MultipleChoice : ActivityType()
    object FillBlank : ActivityType()
    object Matching : ActivityType()
    object DragPairs : ActivityType()
    object SentenceBuilder : ActivityType()
}

data class MatchingPair(
    val leftItem: String,
    val rightItem: String
)

data class OrderedPair(
    val item: String,
    val correctPosition: Int
)

data class ActivityContent(
    val type: ActivityType,
    val title: String,
    val explanation: String? = null,
    val imageRes: Int? = null,
    val question: String? = null,
    val blankPosition: Int? = null,
    val options: List<String>? = null,
    val correctAnswer: String? = null,
    val feedback: String? = null,
    val matchingPairs: List<MatchingPair>? = null,
    val shuffledRightItems: List<String>? = null,
    val orderedPairs: List<OrderedPair>? = null,
    val showNumbers: Boolean = true,
    val sentenceParts: List<String>? = null,
    val correctOrder: List<String>? = null
){
    companion object {
        fun fromMap(map: Map<String, Any>): ActivityContent {
            // Implementar lógica de conversión
            return TODO("Provide the return value")
        }
    }

    fun toMap(): Map<String, Any> {
        // Implementar lógica de conversión
        return TODO("Provide the return value")
    }
}