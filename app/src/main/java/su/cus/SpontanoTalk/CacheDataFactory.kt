package su.cus.SpontanoTalk

import su.cus.SpontanoTalk.API.FilmDescription.FilmDataItem
import android.content.Context
import su.cus.SpontanoTalk.API.CachedData
import su.cus.SpontanoTalk.API.ICachedData
import su.cus.SpontanoTalk.API.MoviesRepository.ListPremiere

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