package br.com.androidprofessional.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.api.Status
import br.com.androidprofessional.data.model.ExampleResponse
import br.com.androidprofessional.domain.usecase.GetExampleUseCase
import br.com.androidprofessional.presentation.model.ObjectPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/*
* Esta classe tem por objetivo conter sua lógica de apresentação.
* Por exemplo, se o texto apresentado será X ou Y, realizar a chamada Coroutines,
* e fornecer os dados formatados para sua View.
*
* Obs1.: É de extrema importância que sua lógica fique acoplada aqui e não na sua Activity.
* Costumamos dizer que sua View deve ser o mais "burra" possível, ou seja, só mostrar/ocultar os dados.
*
* Obs2.: Este projeto está utilizando Coroutines para realizar as chamadas assíncronas.
* É de extrema importância entender sua utilização, opções de threads (por exemplo, utilizei o
* Dispatchers.IO abaixo) e qual o impacto de sua utilização em seu app.
*
*/

class YourViewModel(
    private val useCase: GetExampleUseCase
) : ViewModel() {
//
//    private val _resultSuccess = MutableLiveData<Result<Boolean>>().apply { value = Result.success(false) }
//
//    val resultSuccess : LiveData<Result<Boolean>>
//        get() = _resultSuccess
//
//    fun exampleCallCoroutines() {
//
//        viewModelScope.launch(Dispatchers.IO) {
//
//            runCatching {
//                useCase()
//            }.onSuccess {
//
//                // Do something case successful
//                _resultSuccess.postValue(Result.success(true))
//
//            }.onFailure {
//
//                // Do something case failure
//                _resultSuccess.postValue(Result.failure(it))
//
//            }
//        }
//    }

    /**
     * A commentData mutableStateflow which will store the Helper class CommentApiState
     * which may store the api data or the failure state.
     * Views can collect this data in background, as data is emitted whenever value changes, similar to livedata.
     * Set the initial Value to Loading.
     */
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