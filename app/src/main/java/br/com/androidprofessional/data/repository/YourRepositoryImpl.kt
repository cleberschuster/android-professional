package br.com.androidprofessional.data.repository

import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.datasource.RemoteDataSource
import br.com.androidprofessional.domain.mapper.ObjectToPresentationMapper
import br.com.androidprofessional.domain.repository.YourRepository
import br.com.androidprofessional.presentation.model.ObjectPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/*
* Esta camada é responsável por chavear entre as fontes de dados.
* Por exemplo, se você tivesse armazenamento local (usando ROOM, por exemplo),
* você teria um localDataSource, e este repository seria responsável pela
* lógica de saber se pega os dados localmente ou da API.
*
* Após a chamada, é realizado a transformação do objeto da camada de DOMAIN
* para um objeto da camada de PRESENTATION por meio do mapper.
*
*/

class YourRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : YourRepository {

    private val mapper: ObjectToPresentationMapper = ObjectToPresentationMapper()

    override suspend fun getExample(id: Int): Flow<ExampleApiState<ObjectPresentation>> = flow {
//        try {
            val result = withContext(Dispatchers.IO) {
                mapper.map(remoteDataSource.getExample(id))
            }
            emit(ExampleApiState.success(result))

        }
//        catch (ex: Exception) {
//            emit(ExampleApiState.error(ex.toString()))
//        } catch (ex: Throwable) {
//            emit(ExampleApiState.error(ex.toString()))
//        }
//    }
}