package br.com.androidprofessional.data.api

import br.com.androidprofessional.presentation.model.ObjectPresentation
import retrofit2.HttpException
import java.io.IOException

data class ApiState(
    val status: Status = Status.LOADING,
    val data: ObjectPresentation? = null,
    val message: String ?= null
)

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

fun Throwable.toErrorType(): Any = when (this) {
    is IOException -> ErrorType.Api.Network
    is HttpException -> when (code()) {
//        ErrorCodes.Http.ResourceNotFound -> ErrorType.Api.NotFound
        ErrorCodes.Http.ResourceNotFound -> code()
        ErrorCodes.Http.InternalServer -> ErrorType.Api.Server
        ErrorCodes.Http.ServiceUnavailable -> ErrorType.Api.ServiceUnavailable
        else -> ErrorType.Unknown
    }
    else -> ErrorType.Unknown
}
//
//fun Throwable.toErrorType2(): Any {
//    return when (this) {
//        is IOException -> ErrorType.Api.Network
//        is HttpException -> when (code()) {
////        ErrorCodes.Http.ResourceNotFound -> ErrorType.Api.NotFound
//            ErrorCodes.Http.ResourceNotFound -> code()
//            ErrorCodes.Http.InternalServer -> ErrorType.Api.Server
//            ErrorCodes.Http.ServiceUnavailable -> ErrorType.Api.ServiceUnavailable
//            else -> ErrorType.Unknown
//        }
//
//        else -> ErrorType.Unknown
//    }
//}

object ErrorCodes {

    object Http {
        const val InternalServer = 501
        const val ServiceUnavailable = 503
        const val ResourceNotFound = 404
    }
}

sealed class ErrorType {

    sealed class Api: ErrorType() {

        object Network: Api()

        object ServiceUnavailable : Api()

        object NotFound : Api()

        object Server : Api()

    }

    object Unknown: ErrorType()
}
