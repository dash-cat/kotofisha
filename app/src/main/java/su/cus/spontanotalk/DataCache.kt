package su.cus.spontanotalk
import android.content.Context
import java.io.File
import java.io.IOException // Added for specific IOException catching

class DataCache(private val context: Context) {

    // Function to write data to a cache file
    @Synchronized
    fun writeToCache(fileName: String, data: String) {
        val cacheDir = context.externalCacheDir
        if (cacheDir == null) {
            throw CacheDirectoryException("External cache directory not available.")
        }
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw CacheDirectoryException("Failed to create cache directory: ${cacheDir.absolutePath}")
        }
        if (!cacheDir.canWrite()) {
            throw CacheDirectoryException("Cache directory is not writable: ${cacheDir.absolutePath}")
        }

        val cacheFile = File(cacheDir, fileName)

        try {
            cacheFile.bufferedWriter().use { writer ->
                writer.write(data)
            }
        } catch (e: IOException) {
            throw CacheWriteException("Failed to write to cache file: ${cacheFile.absolutePath}", e)
        } catch (e: Exception) {
            throw CacheWriteException("An unexpected error occurred while writing to cache file: ${cacheFile.absolutePath}", e)
        }
    }

    // Function to read data from a cache file
    @Synchronized
    fun readFromCache(fileName: String): String? {
        val cacheDir = context.externalCacheDir
        if (cacheDir == null || !cacheDir.exists() || !cacheDir.canRead()) {
            // If dir doesn't exist or isn't readable, it's not an error per se, but data isn't there.
            // Consider logging this situation if it's unexpected.
            return null
        }

        val cacheFile = File(cacheDir, fileName)

        if (!cacheFile.exists()) return null
        if (!cacheFile.canRead()) {
            throw CacheReadException("Cache file is not readable: ${cacheFile.absolutePath}")
        }

        return try {
            cacheFile.readText()
        } catch (e: IOException) {
            throw CacheReadException("Failed to read from cache file: ${cacheFile.absolutePath}", e)
        } catch (e: Exception) {
            throw CacheReadException("An unexpected error occurred while reading from cache file: ${cacheFile.absolutePath}", e)
        }
    }
}