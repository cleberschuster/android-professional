package br.com.androidprofessional.utils

sealed class Resource<T>(
    open val data: T?,
    open val error: Throwable? = null
) {

    data class Success<T>(
        override val data: T?
    ) : Resource<T>(data, null)

    data class Error<T>(
        override val data: T?,
        override val error: Throwable?
    ) : Resource<T>(data, error)
}