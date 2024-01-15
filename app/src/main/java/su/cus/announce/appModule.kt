package su.cus.announce

import org.koin.dsl.module
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.ListPremiere
import su.cus.announce.API.RetrofitClient
import su.cus.announce.premiere.CachedData
import su.cus.announce.premiere.PremiereListPresenterImpl
import su.cus.announce.premiere.PremiereListPresenterInput

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory <PremiereListPresenterInput>{ params ->
        val cache = CachedData<ListPremiere>(
            get(),
            ListPremiere.serializer(),
            "movies.cache"
        )
        PremiereListPresenterImpl(get(), params.get(), get(), cache)
    }
    factory {  UserPresenterImpl(get()) }
    single <IRetrofitClient>{ RetrofitClient() }
}