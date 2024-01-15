package su.cus.announce.API

import android.content.Context
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import su.cus.announce.DataCache

class CachedData<T>(
    context: Context,
    private val serializer: KSerializer<T>,
    private val filename: String
): ICachedData<T> {

    private val cache = DataCache(context)
    override fun read(): T? {

        val cachedData = cache.readFromCache(filename) ?: return null
        val cacheJson = Json.decodeFromString(serializer, cachedData)
        println("TUT $cacheJson")
        return cacheJson
    }

    override fun write(obj: T) {

        cache.writeToCache(filename, Json.encodeToString(serializer, obj))
    }
}