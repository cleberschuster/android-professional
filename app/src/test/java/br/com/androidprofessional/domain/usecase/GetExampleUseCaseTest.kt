package br.com.androidprofessional.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.domain.repository.YourRepository
import br.com.androidprofessional.presentation.model.ObjectPresentation
import br.com.androidprofessional.utils.MainCoroutineRule
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import kotlin.Result.Companion.success
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetExampleUseCaseTest {
    private val repository: YourRepository = mockk()
    private val useCase: GetExampleUseCase = GetExampleUseCase(repository)

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `when invoke should return success`() = runBlocking {
        // Given
        coEvery { repository.getExample(1) } returns flowOf(ExampleApiState.success(ObjectPresentation()))

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        // When
        val result = useCase.invoke(1)
        val resultExpected = ExampleApiState.success(ObjectPresentation())

        // Then
        result.test {
            val emission = awaitItem()
            Truth.assertThat(emission).isEqualTo(resultExpected)
            cancelAndIgnoreRemainingEvents()
        }
    }

//    @Test
//    fun `when invoke should return success`() = runBlockingTest {
//        // Given
//        whenever(repository.getExample()).thenAnswer { success(ObjectPresentation(id = "123")) }
//
//        // When
//        val result = useCase.invoke()
//
//        val resultExpected = success(ObjectPresentation(id = "123"))
//
//        // Then
//        assertEquals(result, resultExpected)
//    }
}