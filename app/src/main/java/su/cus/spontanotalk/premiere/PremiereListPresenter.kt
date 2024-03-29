package su.cus.spontanotalk.premiere

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.cus.spontanotalk.API.FilmDescription.FilmDataItem
import su.cus.spontanotalk.API.ICachedData
import su.cus.spontanotalk.API.IRetrofitClient
import su.cus.spontanotalk.API.MoviesRepository.ListPremiere
import su.cus.spontanotalk.ICachedDataFactory

interface PremiereListPresenterInput {
    suspend fun loadMovies()
    suspend fun getMovieById(id: String): FilmDataItem?

}

interface PremiereListPresenterOutput {
    suspend fun showErrorMessage(errorMessage: String?)


    fun showMovies(premiere: ListPremiere )

//    fun sendFilmToDescription(film: FilmDataItem)


}

class PremiereListPresenterImpl(
    val context: Context,
    private val output: PremiereListPresenterOutput,
    private val client: IRetrofitClient,
    private val cacheFactory: ICachedDataFactory
): PremiereListPresenterInput {

    private suspend fun <T> fetchCached(
        fetchData: suspend () -> T,
        makeCache: () -> ICachedData<T>
    ): T {
        val cache = makeCache()
        val cachedData = cache.read()
        return if (cachedData == null) {
            val fetchedData = fetchData()
            cache.write(fetchedData)
            fetchedData
        } else {
            cachedData
        }
    }

    override suspend fun loadMovies() {
        try {
            val movies = fetchCached({
                client.getMovies(2023, "JANUARY")
            }, {
                cacheFactory.makeCachedDataForMoviesList()
            })
            withContext(Dispatchers.Main) {
                output.showMovies(movies)
            }
        } catch (e: Exception) {
            output.showErrorMessage("Ошибка загрузки $e")
        }
    }
    override suspend fun getMovieById(id: String): FilmDataItem? {
        return try {
            val film = fetchCached({
                client.getFilm(id)
            }, {
                cacheFactory.makeCachedDataForMovie(id)
            })
            film // This is the FilmDataItem to return
        } catch (e: Exception) {
            output.showErrorMessage("Ошибка загрузки $e")
            null // Return null in case of an error
        }
    }
}

