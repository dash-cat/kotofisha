package su.cus.spontanotalk.API

import java.io.IOException

sealed class ApiException(message: String, cause: Throwable? = null) : IOException(message, cause) {
    class NetworkException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class NotFoundException(message: String = "Resource not found") : ApiException(message)
    class ServiceUnavailableException(message: String = "Service unavailable") : ApiException(message)
    class UnauthorizedException(message: String = "Unauthorized") : ApiException(message)
    class UnexpectedCodeException(message: String, val code: Int) : ApiException(message)
    class UnknownApiException(message: String, cause: Throwable? = null) : ApiException(message, cause)
}
