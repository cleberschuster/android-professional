package br.com.androidprofessional.data.api

import br.com.androidprofessional.data.model.ExampleResponse
import retrofit2.http.GET
import retrofit2.http.Path

/*
* Esta classe é responsável por conter seus métodos
* de chamada da API (métodos GET, POST, PATCH, etc);
*/

interface YourService {

//    @GET("/endpoint/example")
//    suspend fun getExample(): ExampleResponse

    @GET("/comments/{id}")
    suspend fun getExample(@Path("id") id: Int): ExampleResponse
}