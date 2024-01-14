package su.cus.announce

import org.koin.dsl.module
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.RetrofitClient
import su.cus.announce.premiere.PremiereListPresenterImpl
import su.cus.announce.premiere.PremiereListPresenterInput

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory <PremiereListPresenterInput>{ params -> PremiereListPresenterImpl(get(), params.get(), get()) }
    factory {  UserPresenterImpl(get()) }
    single <IRetrofitClient>{ RetrofitClient() }
}