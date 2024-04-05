package br.com.androidprofessional.utils

sealed class MyState<out T : Any> {
    object Loading : MyState<Nothing>()
    data class Success<out T : Any>(val result : T) : MyState<T>()
    data class Error(val error : Throwable?) : MyState<Nothing>()
}