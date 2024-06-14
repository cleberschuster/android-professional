package br.com.androidprofessional.domain.repository

import br.com.androidprofessional.presentation.model.ObjectPresentation
import br.com.androidprofessional.utils.Resource
import kotlinx.coroutines.flow.Flow

interface YourRepository {
    suspend fun getExample(id: Int): Flow<Resource<ObjectPresentation>>
}