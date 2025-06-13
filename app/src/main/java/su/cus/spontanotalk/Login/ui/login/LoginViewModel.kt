package su.cus.spontanotalk.Login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import su.cus.spontanotalk.Login.AuthSignUpFailedException
import su.cus.spontanotalk.Login.IAuthService
import su.cus.spontanotalk.Login.IUser
import su.cus.spontanotalk.Login.data.LoginRepository
import su.cus.spontanotalk.Login.data.Result // Assuming this is the custom Result class used by LoginRepository
import su.cus.spontanotalk.Login.data.model.LoggedInUser

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val authService: IAuthService // Added for signUp
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _signUpResult = MutableLiveData<LoginResult>() // Using LoginResult for signUp too for simplicity
    val signUpResult: LiveData<LoginResult> = _signUpResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LoginFormState for input validation (can be expanded)
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    fun login(email: String, password: String) {
        // Basic validation (can be moved to a separate validation function)
        if (!isEmailValid(email)) {
            _loginFormState.value = LoginFormState(emailError = "Invalid email format.")
            return
        }
        if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = "Password must be >5 characters.")
            return
        }
        _loginFormState.value = LoginFormState(isDataValid = true) // Data is valid enough to attempt login

        _isLoading.value = true
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            if (result is Result.Success) {
                _loginResult.value = LoginResult.Success(result.data)
            } else if (result is Result.Error) {
                _loginResult.value = LoginResult.Error(result.exception.message ?: "Login failed")
            }
            _isLoading.value = false
        }
    }

    fun signUp(email: String, password: String) {
        if (!isEmailValid(email)) {
            // Handle validation error, perhaps update a form state LiveData
            _signUpResult.value = LoginResult.Error("Invalid email format.")
            return
        }
        if (password.length < 6) { // Example: Firebase min password length
            _signUpResult.value = LoginResult.Error("Password must be at least 6 characters.")
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val user: IUser = authService.signUp(email, password)
                // Assuming IUser can be mapped or indicates success
                // IUser now has uid and email
                _signUpResult.value = LoginResult.Success(LoggedInUser(user.uid, user.email ?: ""))
            } catch (e: AuthSignUpFailedException) {
                _signUpResult.value = LoginResult.Error(e.message ?: "Sign up failed")
            } catch (e: Exception) {
                _signUpResult.value = LoginResult.Error(e.message ?: "An unexpected error occurred during sign up.")
            }
            _isLoading.value = false
        }
    }

    // Simple email validation
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Simple password validation
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5 // Example criteria
    }
}

// Removed placeholder R object
