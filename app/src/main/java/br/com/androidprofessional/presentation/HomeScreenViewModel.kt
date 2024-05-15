package br.com.androidprofessional.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.androidprofessional.data.api.recipeService
import br.com.androidprofessional.data.model.dataclasses.HomeScreenState
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val _homeScreenState = mutableStateOf(HomeScreenState())
    val homeScreenState: State<HomeScreenState> = _homeScreenState

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                _homeScreenState.value =
                    _homeScreenState.value.copy(
                        loading = true,
                        categories = emptyList()
                    )
                val response = recipeService.getCategories()
                _homeScreenState.value = _homeScreenState.value.copy(
                    categories = response.categories,
                    loading = false,
                    error = null,
                )
            } catch (e: Exception) {
                _homeScreenState.value = _homeScreenState.value.copy(
                    loading = false,
                    error = e.message,
                )
            }
        }
    }
}