package br.com.androidprofessional.data.datasource

import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.api.YourService
import br.com.androidprofessional.data.mapper.ObjectToDomainMapper
import br.com.androidprofessional.data.model.ExampleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/*
* Esta classe é responsável por chamar a API.
* Como o resultado retornado pela API é um objeto da camada de DATA, já
* é realizado o map para retornar um objeto da camada de DOMAIN.
*
* Obs.: Muita gente prefere injetar o Mapper direto no construtor, para
* aproveitar o uso do Koin. Esse ponto é válido de discussão.
* Eu, particularmente, acho que é um uso desnecessário de recursos, preferindo não injetar.
*
*/

class RemoteDataSourceImpl(
    private val api: YourService
) : RemoteDataSource {

    private val mapper: ObjectToDomainMapper = ObjectToDomainMapper()

//    override suspend fun getExample() = mapper.map(api.getExample())

    /***
     * Function which will call the api and it will return a Flow.
     * Return a flow, It asynchronously performs calculation or function
     * and the emit function emits the data to the receivers which are listening
     * to this flow.
     */

    override suspend fun getExample(id: Int) = mapper.map(api.getExample(id))

}
