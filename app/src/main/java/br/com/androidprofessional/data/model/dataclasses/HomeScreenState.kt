package br.com.androidprofessional.data.model.dataclasses

data class HomeScreenState(
    val loading: Boolean = false,
    val categories: List<MealCategory> = emptyList(),
    val error: String? = null
)