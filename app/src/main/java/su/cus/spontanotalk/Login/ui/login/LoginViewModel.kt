package su.cus.spontanotalk.Login.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import su.cus.spontanotalk.Login.IAuthService
import su.cus.spontanotalk.Login.data.LoginRepository
import su.cus.spontanotalk.Login.data.Result
import su.cus.spontanotalk.R

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val authService: IAuthService
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isSignInButtonEnabled = MutableLiveData<Boolean>()
    val isSignInButtonEnabled: LiveData<Boolean> = _isSignInButtonEnabled

    fun setSignInButtonClickListener(listener: SignInButtonClickListener) {
        _signInButtonClickListener = listener
    }

    private lateinit var _signInButtonClickListener: SignInButtonClickListener

    interface SignInButtonClickListener {
        fun onSignInButtonClick()
    }

    fun signIn() {
        _signInButtonClickListener.onSignInButtonClick()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}