package su.cus.spontanotalk

import java.io.IOException

sealed class CacheException(message: String, cause: Throwable? = null) : IOException(message, cause) {
    class CacheReadException(message: String, cause: Throwable? = null) : CacheException(message, cause)
    class CacheWriteException(message: String, cause: Throwable? = null) : CacheException(message, cause)
    class CacheDirectoryException(message: String, cause: Throwable? = null) : CacheException(message, cause)
}
