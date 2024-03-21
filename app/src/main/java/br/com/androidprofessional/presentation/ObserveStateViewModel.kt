package br.com.androidprofessional.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.api.Status
import br.com.androidprofessional.domain.usecase.GetExampleUseCase
import br.com.androidprofessional.presentation.model.ObjectPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// VIDEO COMO CRIAR E OBSERVAR ESTADOS EM JETPACK COMPOSE:  https://youtu.be/kuwZX2fSj5A
class ObserveStateViewModel(private val useCase: GetExampleUseCase) : ViewModel() {

//    val loadingStateFlow = MutableStateFlow(false) // IMPORTANT QUE SEJA "FLOW"
//
//    fun setLoadingState(loading: Boolean) {
//        viewModelScope.launch {
//            loadingStateFlow.emit(loading)
//        }
//    }

    val commentState = MutableStateFlow(
        ExampleApiState(
            Status.LOADING,
            ObjectPresentation(), ""
        )
    )

    init {
        //Initiate a starting search with comment Id 1
        getNewComment(1)
    }


    //Function to get new Comments
    fun getNewComment(id: Int) {

        //Since Network Calls takes time,Set the initial value to loading state
        commentState.value = ExampleApiState.loading()

        //ApiCalls takes some time, So it has to be run and background thread. Using viewModelScope to call the api
        viewModelScope.launch(Dispatchers.IO) {

            //Collecting the data emitted by the function in repository

            useCase.invoke(id)
                //If any errors occurs like 404 not found or invalid query, set the state to error State to show some info
                //on screen
                .catch {
                    commentState.value =
                        ExampleApiState.error(it.message.toString())
                }
                //If Api call is succeeded, set the State to Success and set the response data to data received from api
                .collect {
                    commentState.value = ExampleApiState.success(it.data)
                }
        }
    }
}