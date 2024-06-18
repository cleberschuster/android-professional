package com.example.search.screens.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.search.Screen
import com.example.search.dataclasses.MealCategory
import com.example.search.screens.categorydetails.CategoryDetailsScreen
import com.example.search.screens.home.HomeScreen
import com.example.search.screens.home.HomeScreenViewModel

@Composable
fun RootScreen() {
    val navController = rememberNavController()
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val homeScreenState by homeScreenViewModel.homeScreenState

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {

        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                viewState = homeScreenState,
                navigateToCategoryDetailsScreen = { category ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("category", category)
                    navController.navigate(Screen.MealCategoryDetailsScreen.route)
                }
            )
        }

        composable(route = Screen.MealCategoryDetailsScreen.route) {
            val category =
                navController.previousBackStackEntry?.savedStateHandle?.get<MealCategory>("category")
                    ?: dummyMealCategory
            CategoryDetailsScreen(
                category = category,
                navigateToHomeScreen = { navController.popBackStack() }
            )
        }
    }
}

val dummyMealCategory = MealCategory(
    idCategory = "",
    strCategory = "",
    strCategoryThumb = "",
    strCategoryDescription = ""
)