package br.com.androidprofessional.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.api.Status
import br.com.androidprofessional.domain.usecase.GetExampleUseCase
import br.com.androidprofessional.presentation.model.ObjectPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
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
        //Initiate a starting search with comment Id 1
        getNewComment(5)
    }


    //Function to get new Comments
    fun getNewComment(id: Int) {
        //Since Network Calls takes time,Set the initial value to loading state
        _uiState.update { it.copy(Status.LOADING) }
//        _uiState.value = ExampleApiState.loading()

        //ApiCalls takes some time, So it has to be run and background thread. Using viewModelScope to call the api
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            //Collecting the data emitted by the function in repository

            useCase.invoke(id)
//                .onStart {  }
//                .onCompletion { }

                //If any errors occurs like 404 not found or invalid query, set the state to error State to show some info
                //on screen
                .catch {
                        //Assim tambem funciona, avaliar se com o copyc é o melhor
//                    _uiState.value = ExampleApiState.error(it.message.toString())

                    _uiState.update { uiState ->
                        uiState.copy(
                            status = Status.ERROR,
                            data = null,
                            message = it.message
                        )
                    }
                }
                //If Api call is succeeded, set the State to Success and set the response data to data received from api
                .collect { response ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            status = Status.SUCCESS,
                            data = response.data,
                            message = null
                        )
                    }
                }
        }
    }
}
