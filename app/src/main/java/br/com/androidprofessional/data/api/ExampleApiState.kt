package br.com.androidprofessional.data.api

import retrofit2.HttpException
import java.io.IOException


//A helper class to handle states
data class ExampleApiState<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        //In case of Success,set status as Success and data as the response
        fun <T> success(data: T?): ExampleApiState<T> {
            return ExampleApiState(Status.SUCCESS, data, null)
        }

        //In case of failure ,set state to Error ,add the error message,set data to null
        fun <T> error(msg: String): ExampleApiState<T> {
            return ExampleApiState(Status.ERROR, null, msg)
        }

        //When the call is loading set the state as Loading and rest as null
        fun <T> loading(): ExampleApiState<T> {
            return ExampleApiState(Status.LOADING, null, null)
        }

    }

}

//An enum to store the current state of api call
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
