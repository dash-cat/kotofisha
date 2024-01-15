package su.cus.announce.premiere

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.cus.announce.API.ICachedData
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.ListPremiere

interface PremiereListPresenterInput {
    suspend fun loadMovies()

}

interface PremiereListPresenterOutput {
    suspend fun showErrorMessage(errorMessage: String?)

    fun showMovies(premiere: ListPremiere )


}

class PremiereListPresenterImpl(
    val context: Context,
    private val output: PremiereListPresenterOutput,
    private val client: IRetrofitClient,
    private val cache: ICachedData<ListPremiere>,
): PremiereListPresenterInput {

    override suspend fun loadMovies() {

        val cachedData = cache.read()
        withContext(Dispatchers.Main) {
            if (cachedData == null) {
                println("Fetch: $cachedData")
                fetchMoviesFromNetwork()
            } else {
                output.showMovies(cachedData)
            }
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