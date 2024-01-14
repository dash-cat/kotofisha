package su.cus.announce

import org.koin.dsl.module
import su.cus.announce.API.PremiereListPresenterImpl
import su.cus.announce.API.PremiereListPresenterInput

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory <PremiereListPresenterInput>{ PremiereListPresenterImpl(get(), get()) }
    factory {  UserPresenterImpl(get()) }
}