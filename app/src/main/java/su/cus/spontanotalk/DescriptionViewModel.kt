package su.cus.spontanotalk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import su.cus.spontanotalk.API.IRetrofitClient

class DescriptionViewModel(
    private val movieId: String,
    private val client: IRetrofitClient
): ViewModel() {
    val title = MutableLiveData<String>()
    val posterUrl = MutableLiveData<String?>()
    val releaseYear = MutableLiveData<String>()
    val descriptionText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String?>()
    val genreText = MutableLiveData<String>()
    val ratingKinopoisk = MutableLiveData<Double>()
    val ratingImdb = MutableLiveData<Double>()

    suspend fun loadDescription() {
        try {
            val film = client.getFilm(movieId)
            title.postValue(film.nameRu ?: "Без названия")
            posterUrl.postValue(film.posterUrl)
            releaseYear.postValue(film.year?.let { "$it" } ?: "Год выхода неизвестен")
            descriptionText.postValue(film.description ?: "Нет описания")
            ratingKinopoisk.postValue((film.ratingKinopoisk ?: "Нет данных") as Double?)
            ratingImdb.postValue((film.ratingImdb ?: "нет данных") as Double?)
            val genreString = film.genres?.joinToString(separator = ", ") { genre ->
                genre.genre
            } ?: "Нет информации о жанре"
            genreText.postValue(genreString)

        } catch (e: Exception) {
            errorMessage.postValue(e.toString())
        }
    }
}