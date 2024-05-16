package br.com.androidprofessional.presentation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object MealCategoryDetailsScreen : Screen("mealCategoryDetails")
}