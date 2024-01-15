package su.cus.announce.premiere

import android.content.Context
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.DataCache

interface PremiereListPresenterInput {
    suspend fun loadMovies()

}

interface PremiereListPresenterOutput {
    suspend fun showErrorMessage(errorMessage: String?)

    fun showMovies(moviesList: List<Movie> )

}
interface ICachedData<T> {
    fun read(): T?

    fun write(obj:T)
}

class CachedData<T>(
    context: Context,
    private val serializer: KSerializer<T>,
    private val filename: String
): ICachedData<T> {

    private val cache = DataCache(context)
    override fun read(): T? {

        val cachedData = cache.readFromCache(filename) ?: return null
        println("TUT")
        println(Json.decodeFromString(serializer, cachedData))
        return Json.decodeFromString(serializer, cachedData)
    }

    override fun write(obj: T) {
        cache.writeToCache(filename, Json.encodeToString(serializer, obj))
    }
}

class PremiereListPresenterImpl(
    val context: Context,
    private val output: PremiereListPresenterOutput,
    private val client: IRetrofitClient,
    private val cache: ICachedData<List<Movie>>,
): PremiereListPresenterInput {

    override suspend fun loadMovies() {
        val cachedData = cache.read()
        println("Cache 2: $cachedData")
        println("Cache 3: $cache")
        if (cachedData == null) {
            fetchMoviesFromNetwork()
        } else {
            output.showMovies(cachedData)
        }
    }

    private suspend fun fetchMoviesFromNetwork() {
        try {
            val movies = client.getMovies(2023, "JANUARY")
            println("movies 2: $movies")
            output.showMovies(movies)
            cache.write(movies)
        } catch (e: Exception) {
            output.showErrorMessage("Ошибка загрузки $e")
        }
    }
}