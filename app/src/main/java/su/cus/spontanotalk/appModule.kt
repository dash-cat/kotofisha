package su.cus.spontanotalk

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.dsl.module
import su.cus.spontanotalk.API.IRetrofitClient
import su.cus.spontanotalk.API.RetrofitClient
import su.cus.spontanotalk.Login.FireBaseAuthService
import su.cus.spontanotalk.Login.IAuthService
import su.cus.spontanotalk.premiere.PremiereListPresenterImpl
import su.cus.spontanotalk.premiere.PremiereListPresenterInput

val appModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    factory <PremiereListPresenterInput>{ params ->
        PremiereListPresenterImpl(get(), params.get(), get(), get())
    }
    factory { UserPresenterImpl(get()) }
    factory { params -> DescriptionViewModel(params.get(), get()) }
    factory <ISignUpOpener> {
        MainActivity.instance ?: throw Exception("Main activity not ready yet")
    }
    single <ICachedDataFactory>{ CachedDataFactory(get()) }
    single <IRetrofitClient>{ RetrofitClient() }
    single <IAuthService> { FireBaseAuthService(Firebase.auth, null) }
}