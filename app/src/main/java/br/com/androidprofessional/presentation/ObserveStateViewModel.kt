package br.com.androidprofessional.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.api.Status
import br.com.androidprofessional.data.api.toErrorType
import br.com.androidprofessional.domain.usecase.GetExampleUseCase
import br.com.androidprofessional.presentation.model.ObjectPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ObserveStateViewModel(private val useCase: GetExampleUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ExampleApiState(
            status =  Status.LOADING,
            data =  ObjectPresentation(),
            message = null
        )
    )

    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = _uiState.value
    )

    init {
        getNewComment(5)
    }

    fun getNewComment(id: Int) {
        _uiState.update { it.copy(Status.LOADING) }

        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)

            useCase.invoke(id)
//              .onStart {  }
//              .onCompletion { }

                //Trata erros de downstream
                .onEach {
                    _uiState.update { currentState ->
                        currentState.copy(
                            status = Status.SUCCESS,
                            data = it.data,
                        )
                    }
                }
                //Trata erros de upstream
                .catch {
                    if (it.toErrorType().toString() == "404") {
                        _uiState.update { currentState ->
                            currentState.copy(
                                status = Status.ERROR,
//                            message = it.message
                                message = " 404, faça a logica com o tipo do erro"
                            )
                        }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                status = Status.ERROR,
//                            message = it.message
                                message = it.toErrorType().toString()
                            )
                        }
                    }
                }
                // Substitui o collect() e possibilita tratar excessoes de downstream quando usamos o .onEach()
                .launchIn(this)
        }
    }
}
