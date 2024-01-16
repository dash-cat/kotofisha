package su.cus.announce

import su.cus.announce.API.FilmDescription.FilmDataItem
import android.content.Context
import su.cus.announce.API.CachedData
import su.cus.announce.API.ICachedData
import su.cus.announce.API.MoviesRepository.ListPremiere

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