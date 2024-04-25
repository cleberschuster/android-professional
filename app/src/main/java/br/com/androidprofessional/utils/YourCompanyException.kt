package br.com.androidprofessional.utils

class YourCompanyException(
    message: String? = null,
    val apiError: ApiError? = null
) : Exception(message)