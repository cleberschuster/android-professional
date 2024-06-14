package br.com.androidprofessional.utils

sealed class Resource<T>(
    open val data: T?,
) {
    data class Success<T>(
        override val data: T?
    ) : Resource<T>(data)
}