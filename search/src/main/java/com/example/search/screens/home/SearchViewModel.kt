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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())

    private val _textoDigitado = MutableStateFlow("")
    val textoDigitado = _textoDigitado.asStateFlow()

    private val _listaOriginal = MutableStateFlow(HomeScreenState().categories)

    val listaFiltrada: StateFlow<List<MealCategory>> =
        _textoDigitado.combine(_listaOriginal) { texto, lista ->
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
        _textoDigitado.value = novoTexto
    }

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