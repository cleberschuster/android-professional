package br.com.androidprofessional.data.api


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
