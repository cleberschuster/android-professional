package br.com.androidprofessional.data.datasource

import br.com.androidprofessional.domain.model.ObjectDomain

interface RemoteDataSource {

//    suspend fun getExample(): ObjectDomain

    suspend fun getExample(id: Int): ObjectDomain
}