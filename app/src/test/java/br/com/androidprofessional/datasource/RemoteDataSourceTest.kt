package br.com.androidprofessional.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.data.datasource.RemoteDataSource
import br.com.androidprofessional.data.repository.YourRepositoryImpl
import br.com.androidprofessional.domain.model.ObjectDomain
import br.com.androidprofessional.domain.repository.YourRepository
import br.com.androidprofessional.presentation.model.ObjectPresentation
import br.com.androidprofessional.utils.MainCoroutineRule
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    private val dataSource: RemoteDataSource = mockk()

    private val repository: YourRepository by lazy {
        YourRepositoryImpl(dataSource)
    }

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `When get from remote data source should return success`() = runBlocking {
        // Given
        coEvery { dataSource.getExample(1) } returns ObjectDomain()

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        // When
        val result = repository.getExample(1)
        val resultExpected = ExampleApiState.success(ObjectPresentation())

        // Then
        result.test {
            val emission = awaitItem()
            Truth.assertThat(emission).isEqualTo(resultExpected)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { dataSource.getExample(1) }
    }


//    @Test
//    fun `When get from remote data source should return success`() = runBlockingTest {
//        // Given
//        whenever(dataSource.getExample()).thenReturn(ObjectDomain(id = "12"))
//
//        // When
//        val result = repository.getExample()
//
//        // Then
//        verify(dataSource).getExample()
//        assertEquals("12", result.getOrNull()!!.id)
//    }
}