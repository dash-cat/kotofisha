package su.cus.announce.API

import android.content.Context
import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.API.MoviesRepository.MoviesPremiere
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
    val output: PremiereListPresenterOutput
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
        RetrofitClient.instance.getMovies(2023, "JANUARY").enqueue(object:
            Callback<MoviesPremiere> {
            override fun onResponse(call: Call<MoviesPremiere>, response: Response<MoviesPremiere>) {
                if (response.isSuccessful) {
                    val moviesList = response.body()?.items ?: emptyList()
                    cache.writeToCache(filename, Json.encodeToString(moviesList))

                } else {
                    output.showErrorMessage(response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<MoviesPremiere>, t: Throwable) {
                output.showErrorMessage(t.message)
                Log.d("PremiereList", "Network request failed: ${t.message}")
            }
        })
    }
    private fun displayMoviesFromCache(cachedData: String) {
        output.showMovies(Json.decodeFromString<List<Movie>>(cachedData))
    }
}