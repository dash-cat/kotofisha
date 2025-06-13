package su.cus.spontanotalk.Login

import java.io.IOException

/**
 * Base class for authentication related exceptions.
 */
sealed class AuthException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Thrown when a sign-up operation fails.
 */
class AuthSignUpFailedException(message: String, cause: Throwable? = null) : AuthException(message, cause)

/**
 * Thrown when a login operation fails.
 */
class AuthLoginFailedException(message: String, cause: Throwable? = null) : AuthException(message, cause)

/**
 * Thrown when a user is not found.
 */
class UserNotFoundException(message: String = "User not found") : AuthException(message)
