package su.cus.announce

import org.koin.dsl.module
import su.cus.announce.API.PremiereListPresenterImpl

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory { PremiereListPresenterImpl(get()) }
    factory {  UserPresenterImpl(get()) }
}