package br.com.androidprofessional.domain.repository

import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.presentation.model.ObjectPresentation
import kotlinx.coroutines.flow.Flow

interface YourRepository {

    suspend fun getExample(id: Int): Flow<ExampleApiState<ObjectPresentation>>
}