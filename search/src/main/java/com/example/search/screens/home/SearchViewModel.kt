package com.example.search.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.api.recipeService
import com.example.search.dataclasses.HomeScreenState
import com.example.search.dataclasses.MealCategory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

//    private val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()

    //third state the list to be filtered
    private val _countriesList = MutableStateFlow(_homeScreenState.value.categories)
    val countriesList = searchText
        .combine(_countriesList) { text, countries ->//combine searchText with _contriesList
            if (text.isBlank()) { //return the entery list of countries if not is typed
                countries
            } else {
            countries.filter { country ->// filter and return a list of countries based on the text the user typed
                // This function is a costumize search
//                doesMatchSearchQuery(country, text)
                // This is a simple search
                country.strCategory.contains(text.trim(), ignoreCase = true)
            }
            }
        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = _countriesList.value
        )


    // Outra forma de executar a busca, se necessario com delay
//    @OptIn(FlowPreview::class)
//    val countriesList = searchText
//        .debounce(1000L)
//        .onEach { _isSearching.update { true } }
//        .combine(_countriesList) { text, persons ->
//            if(text.isBlank()) {
//                persons
//            } else {
//                delay(2000L)
//                persons.filter {
//                    doesMatchSearchQuery(it, text)
//                }
//            }
//        }
//        .onEach { _isSearching.update { false } }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            _countriesList.value
//        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                _homeScreenState.update {
                    it.copy(
                        loading = true,
                        categories = emptyList()
                    )
                }
                val response = recipeService.getCategories()
                _homeScreenState.update {
                    it.copy(
                        categories = response.categories,
                        loading = false,
                        error = null,
                    )
                }

                _countriesList.value = _homeScreenState.value.categories

            } catch (e: Exception) {
                _homeScreenState.update {
                    it.copy(
                        loading = false,
                        error = e.message,
                    )
                }
            }
        }
    }
}

fun doesMatchSearchQuery(mealCategory: MealCategory, query: String): Boolean {
    val matchingCombinations = listOf(
        "${mealCategory.idCategory}${mealCategory.strCategory}",
        "$mealCategory.idCategory $mealCategory.strCategory",
        "${mealCategory.idCategory.first()} ${mealCategory.strCategory.first()}",
    )

    return matchingCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}