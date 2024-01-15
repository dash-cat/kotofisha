package su.cus.announce.premiere

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.cus.announce.API.CachedData
import su.cus.announce.API.FilmById.FilmDataItem
import su.cus.announce.API.ICachedData
import su.cus.announce.API.IRetrofitClient
import su.cus.announce.API.MoviesRepository.ListPremiere

interface PremiereListPresenterInput {
    suspend fun loadMovies()
    suspend fun getMovieById(id: String)

}

interface PremiereListPresenterOutput {
    suspend fun showErrorMessage(errorMessage: String?)


    fun showMovies(premiere: ListPremiere )


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
    override suspend fun getMovieById(id: String) {
        try {
            val film = fetchCached({
                client.getFilm(id)
            }, {
                cacheFactory.makeCachedDataForMovie(id)
            })
            ////////
        } catch (e: Exception) {
            output.showErrorMessage("Ошибка загрузки $e")
        }
    }
}

interface ICachedDataFactory {
    fun makeCachedDataForMoviesList(): ICachedData<ListPremiere>
    fun makeCachedDataForMovie(movieId: String): ICachedData<FilmDataItem>
}

class CachedDataFactory(
    private val context: Context
): ICachedDataFactory {
    override fun makeCachedDataForMoviesList(): ICachedData<ListPremiere> {
        return CachedData(
            context,
            ListPremiere.serializer(),
            "movies.cache"
        )
    }
    override fun makeCachedDataForMovie(movieId: String): ICachedData<FilmDataItem> {
        return CachedData(
            context,
            FilmDataItem.serializer(),
            "movie.$movieId.cache"
        )
    }

}