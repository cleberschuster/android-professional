package com.example.search.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.api.recipeService
import com.example.search.dataclasses.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    //third state the list to be filtered
    private val _countriesList = MutableStateFlow(_homeScreenState.value.categories)
    val countriesList = searchText
        .combine(_countriesList) { text, countries ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                countries
            } else {
            countries.filter { country ->// filter and return a list of countries based on the text the user typed
                country.strCategory.contains(text.trim().lowercase(), ignoreCase = true)
            }
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _countriesList.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

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

                _countriesList.value = _homeScreenState.value.categories

            } catch (e: Exception) {
                _homeScreenState.value = _homeScreenState.value.copy(
                    loading = false,
                    error = e.message,
                )
            }
        }
    }
}