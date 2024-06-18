package com.example.search.dataclasses

data class HomeScreenState(
    val loading: Boolean = false,
    val categories: List<MealCategory> = emptyList(),
    val error: String? = null
)