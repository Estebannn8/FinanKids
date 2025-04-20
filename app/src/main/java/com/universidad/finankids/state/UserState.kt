package com.universidad.finankids.state

import com.universidad.finankids.data.model.UserData

data class UserState(
    val userData: UserData = UserData(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentSectionIndex: Int = 0
)