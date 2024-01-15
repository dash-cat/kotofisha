package su.cus.announce
import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class DataCache(private val context: Context) {

    // Функция для записи данных в файл кэша
    fun writeToCache(fileName: String, data: String) {
        val cacheDir = context.externalCacheDir
        val cacheFile = File(cacheDir, fileName)

        try {
            FileWriter(cacheFile).use { writer ->
                writer.write(data)
                writer.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Функция для чтения данных из файла кэша
    fun readFromCache(fileName: String): String? {
        val cacheDir = context.externalCacheDir
        if (cacheDir?.exists() != true || !cacheDir.canWrite()) {

            return null
        }

        val cacheFile = File(cacheDir, fileName)
        println("Cache directory: $cacheDir")
        println("Cache file: $cacheFile")
        println("Cache file exists: ${cacheFile.exists()}")
        println("Cache directory writable: ${cacheDir.canWrite()}")
        if (!cacheFile.exists()) return null

        return try {
            BufferedReader(FileReader(cacheFile)).use { reader ->
                val text = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    text.append(line)
                }
                println("Text $text")
                text.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}