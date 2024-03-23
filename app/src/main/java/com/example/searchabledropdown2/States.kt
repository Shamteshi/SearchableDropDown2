package com.example.searchabledropdown2

data class States(
    val result: List<Int> = emptyList(),
    val loadingNumbers: Boolean = false,
    val selectedNumber: Int? = null
)
