package su.cus.spontanotalk.Login.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import su.cus.spontanotalk.Login.FireBaseAuthService
import su.cus.spontanotalk.Login.IAuthService
import su.cus.spontanotalk.Login.data.LoginDataSource
import su.cus.spontanotalk.Login.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor.
 *
 * This factory is simplified. In a real app with DI (like Koin),
 * Koin would typically handle ViewModel creation and dependency injection.
 */
@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Dependencies for LoginViewModel
            val loginDataSource = LoginDataSource()
            val loginRepository = LoginRepository(applicationContext, loginDataSource)
            val authService: IAuthService = FireBaseAuthService(FirebaseAuth.getInstance())

            return LoginViewModel(loginRepository, authService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}