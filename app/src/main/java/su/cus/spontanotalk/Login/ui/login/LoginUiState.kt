package su.cus.spontanotalk.Login.ui.login

import su.cus.spontanotalk.Login.data.model.LoggedInUser

/**
 * Represents the result of a login attempt to be observed by the UI.
 */
sealed class LoginResult {
    data class Success(val user: LoggedInUser) : LoginResult()
    data class Error(val error: String) : LoginResult() // String message for simplicity
}

/**
 * Represents the state of the login form, typically for input validation.
 * Simplified for now.
 */
data class LoginFormState(
    val emailError: String? = null, // Direct error message string
    val passwordError: String? = null, // Direct error message string
    val isDataValid: Boolean = false
)
