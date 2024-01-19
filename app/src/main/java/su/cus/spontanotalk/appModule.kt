package su.cus.spontanotalk

import org.koin.dsl.module
import su.cus.spontanotalk.API.IRetrofitClient
import su.cus.spontanotalk.API.RetrofitClient
import su.cus.spontanotalk.premiere.PremiereListPresenterImpl
import su.cus.spontanotalk.premiere.PremiereListPresenterInput

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory <PremiereListPresenterInput>{ params ->
        PremiereListPresenterImpl(get(), params.get(), get(), get())
    }
    factory { UserPresenterImpl(get()) }
    factory { params -> DescriptionViewModel(params.get(), get()) }
    single <ICachedDataFactory>{ CachedDataFactory(get()) }
    single <IRetrofitClient>{ RetrofitClient() }
}