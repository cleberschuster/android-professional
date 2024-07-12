package com.example.search.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.search.api.recipeService
import com.example.search.dataclasses.HomeScreenState
import com.example.search.dataclasses.MealCategory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModelRecomended : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())

    private val _searchText = mutableStateOf(value = "")
    val searchText: State<String> = _searchText

    private val _originalList = MutableStateFlow(HomeScreenState().categories)
    private val _filteredList = MutableStateFlow(listOf<MealCategory>())
    val listaFiltrada = _filteredList.asStateFlow()

    fun updateSearchText(newText: String) {
        _searchText.value = newText
        Log.d("SearchViewModel", "updateSearchText: $newText")

        val filtered = if (newText.isEmpty()) {
            _originalList.value
        } else {
            _originalList.value.filter { it.strCategory.contains(newText, ignoreCase = true) }
        }
        _filteredList.value = filtered
    }

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            Log.d("SearchViewModel", "fetchCategories()")
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

                _originalList.value = _homeScreenState.value.categories
                _filteredList.value = _originalList.value

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


//Esta utiliza o combine pra filtrar a lista original
class SearchViewModel : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

//    private val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()

    private val _listaOriginal = MutableStateFlow(HomeScreenState().categories)

    val listaFiltrada: StateFlow<List<MealCategory>> =
        _searchText.combine(_listaOriginal) { texto, lista ->
            if (texto.isBlank()) {
                lista
            } else {
                lista.filter {
                    // filtro simples
                    it.strCategory.contains(texto, ignoreCase = true)
                    // filtro customizado
//                    doesMatchSearchQuery(it, texto)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onTextChanged(novoTexto: String) {
        _searchText.value = novoTexto
    }

    // Outra forma de executar a busca, se necessario com delay
//    @OptIn(FlowPreview::class)
//    val countriesList = _textoDigitado
//        .debounce(1000L)
//        .onEach { _isSearching.update { true } }
//        .combine(_listaOriginal) { text, persons ->
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
//            _listaOriginal.value
//        )

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

                _listaOriginal.value = _homeScreenState.value.categories

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