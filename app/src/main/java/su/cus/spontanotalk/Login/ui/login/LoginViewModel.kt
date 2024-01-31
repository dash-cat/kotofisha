package su.cus.spontanotalk.Login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import su.cus.spontanotalk.Login.data.LoginDataSource
import  su.cus.spontanotalk.Login.data.Result
class LoginViewModel : ViewModel() {

    private val loginDataSource = LoginDataSource()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = loginDataSource.login(email, password)
            when (result) {
                is Result.Success -> {
                    println(">Отправить на другой фрагмент")
                }
                is Result.Error -> {
                    throw Error("Не удалось авторизоваться")
                }
            }
        }
    }


    fun logout() {
        loginDataSource.logout()
    }
}
