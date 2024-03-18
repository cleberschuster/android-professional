package br.com.androidprofessional.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.androidprofessional.data.api.ExampleApiState
import br.com.androidprofessional.domain.usecase.GetExampleUseCase
import br.com.androidprofessional.presentation.model.ObjectPresentation
import br.com.androidprofessional.utils.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class YourViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val useCase: GetExampleUseCase = mockk()
    private val viewModel: YourViewModel by lazy {
        YourViewModel(useCase)
    }

    @Test
    fun `exampleCallCoroutines should call use case`() = runBlocking {
        // Given
        coEvery { useCase.invoke(1) } returns flowOf(ExampleApiState.success(ObjectPresentation()))

        // When
        viewModel.getNewComment(1)

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.commentState
        val resultExpected = ExampleApiState.success(ObjectPresentation())

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(resultExpected)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { useCase.invoke(1) }
    }

//    @Test
//    fun `exampleCallCoroutines should call use case`() = runBlockingTest {
//        // Given
//        whenever(useCase.invoke()).thenAnswer { success(ObjectPresentation(id = "123")) }
//
//        // When
//        viewModel.exampleCallCoroutines()
//
//        val response = viewModel.resultSuccess.value!!.isSuccess
//
//        // Then
//        verify(useCase).invoke()
//        assertTrue(response)
//    }
}
