package su.cus.announce.premiere

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.DataCache

interface PremiereListPresenterInput {
    fun loadMovies()

}

interface PremiereListPresenterOutput {
    fun showErrorMessage(errorMessage: String?)

    fun showMovies(moviesList: List<Movie> )

}

class PremiereListPresenterImpl(
    val context: Context,
    private val output: PremiereListPresenterOutput,
    private val client: IRetrofitClient
): PremiereListPresenterInput {

    private val filename = "movies.cache"
    private val cache = DataCache(context)
    override fun loadMovies() {
        val cachedData = cache.readFromCache(filename)
        if (cachedData.isNullOrEmpty()) {
            fetchMoviesFromNetwork()
        } else {
            displayMoviesFromCache(cachedData)
        }
    }

    private fun fetchMoviesFromNetwork() {
        client.getMovies(2023, "JANUARY") { result ->
            result.fold({
                output.showMovies(it)
            }, {
                output.showErrorMessage("Ошибка загрузки")
            })
        }
    }
    private fun displayMoviesFromCache(cachedData: String) {
        output.showMovies(Json.decodeFromString<List<Movie>>(cachedData))
    }
}