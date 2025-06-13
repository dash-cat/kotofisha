package su.cus.spontanotalk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import su.cus.spontanotalk.API.ApiException
import su.cus.spontanotalk.API.IRetrofitClient

class DescriptionViewModel(
    private val movieId: String,
    private val client: IRetrofitClient
): ViewModel() {
    val title = MutableLiveData<String>()
    val posterUrl = MutableLiveData<String?>()
    val releaseYear = MutableLiveData<String>()
    val descriptionText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String?>() // To signal errors to the UI
    val genreText = MutableLiveData<String>()
    val ratingKinopoisk = MutableLiveData<Double?>() // Changed to Double?
    val ratingImdb = MutableLiveData<Double?>() // Changed to Double?

    fun loadDescription() { // No longer suspend
        viewModelScope.launch {
            try {
                val film = client.getFilm(movieId)
                title.postValue(film.nameRu ?: "Без названия")
                posterUrl.postValue(film.posterUrl)
                releaseYear.postValue(film.year?.let { "$it" } ?: "Год выхода неизвестен")
                descriptionText.postValue(film.description ?: "Нет описания")

                // Post the Double? directly, or null if it's not available
                ratingKinopoisk.postValue(film.ratingKinopoisk)
                ratingImdb.postValue(film.ratingImdb)

                val genreString = film.genres?.joinToString(separator = ", ") { genre ->
                    genre.genre
                } ?: "Нет информации о жанре"
                genreText.postValue(genreString)
                errorMessage.postValue(null) // Clear any previous error message on success
            } catch (e: ApiException) {
                // Handle specific API exceptions
                errorMessage.postValue("API Error: ${e.message}")
            } catch (e: Exception) {
                // Handle other generic exceptions
                errorMessage.postValue("An unexpected error occurred: ${e.message}")
            }
        }
    }
}