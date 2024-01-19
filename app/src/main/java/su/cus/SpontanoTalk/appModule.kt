package su.cus.SpontanoTalk

import org.koin.dsl.module
import su.cus.SpontanoTalk.API.IRetrofitClient
import su.cus.SpontanoTalk.API.RetrofitClient
import su.cus.SpontanoTalk.premiere.PremiereListPresenterImpl
import su.cus.SpontanoTalk.premiere.PremiereListPresenterInput

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