package com.example.search

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object MealCategoryDetailsScreen : Screen("mealCategoryDetails")
}