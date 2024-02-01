package su.cus.spontanotalk.Login.ui.login

import androidx.lifecycle.ViewModel
import su.cus.spontanotalk.Login.data.LoginDataSource

class LoginViewModel : ViewModel() {

    private val loginDataSource = LoginDataSource()

    var onLoading: (() -> Unit)? = null
    var onSuccess: ((String) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null

    fun getPicture() {
        onLoading?.invoke()
        // Здесь код для загрузки данных
        try {
            val data = "Loaded Data"
            onSuccess?.invoke(data)
        } catch (e: Throwable) {
            onError?.invoke(e)
        }
    }


}
