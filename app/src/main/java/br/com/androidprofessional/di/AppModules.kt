package br.com.androidprofessional.di

import br.com.androidprofessional.data.api.YourService
import br.com.androidprofessional.data.datasource.RemoteDataSource
import br.com.androidprofessional.data.datasource.RemoteDataSourceImpl
import br.com.androidprofessional.data.repository.YourRepositoryImpl
import br.com.androidprofessional.data.retrofit.HttpClient
import br.com.androidprofessional.data.retrofit.RetrofitClient
import br.com.androidprofessional.domain.repository.YourRepository
import br.com.androidprofessional.domain.usecase.GetExampleUseCase
import br.com.androidprofessional.presentation.YourViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/*
* Neste arquivo você deve declarar todas as suas dependências injetadas.
* Lembre-se de manter cada classe em sua camada, como feito abaixo.
* Obs.: Se o arquivo ficar muito grande, é melhor criar um arquivo para cada camada.
*/

val domainModules = module {
    factory { GetExampleUseCase(repository = get()) }
}

val presentationModules = module {
    viewModel { YourViewModel(useCase = get()) }
}

val dataModules = module {
    factory<RemoteDataSource> { RemoteDataSourceImpl(api = get()) }
    factory<YourRepository> { YourRepositoryImpl(remoteDataSource = get()) }
}

val networkModules = module {
    single { RetrofitClient(application = androidContext()).newInstance() }
    single { HttpClient(get()) }
    factory { get<HttpClient>().create(YourService::class.java) }
}

val anotherModules = module {}
