package su.cus.announce
import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.FileReader
import java.io.BufferedReader

class DataCache(context: Context) {
    val context = context

    // Функция для записи данных в файл кэша
    fun writeToCache(fileName: String, data: String) {
        val cacheDir = context.externalCacheDir
        val cacheFile = File(cacheDir, fileName)

        try {
            val writer = FileWriter(cacheFile)
            writer.write(data)
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Функция для чтения данных из файла кэша
    fun readFromCache(fileName: String): String? {
        val cacheDir = context.externalCacheDir
        val cacheFile = File(cacheDir, fileName)

        try {
            val reader = BufferedReader(FileReader(cacheFile))
            val text = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                text.append(line)
            }
            reader.close()
            return text.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}