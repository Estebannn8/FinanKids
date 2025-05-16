package com.universidad.finankids.data.model

data class ActivityContent @JvmOverloads constructor(
    val type: ActivityType = ActivityType.Teaching,
    val title: String = "",
    val explanation: String? = null,
    val question: String? = null,
    val options: List<String>? = null,
    val correctAnswer: String? = null,
    val matchingPairs: List<MatchingPair>? = null,
    val orderedPairs: List<OrderedPair>? = null,
    val sentenceParts: List<String>? = null,
    val correctOrder: List<String>? = null,
    val showNumbers: Boolean = false,
    val shuffledRightItems: List<String>? = null
)