package su.cus.announce

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import su.cus.announce.API.IRetrofitClient

class DescriptionViewModel(
    private val movieId: String,
    private val client: IRetrofitClient
): ViewModel() {
    val title = MutableLiveData<String>()
    val posterUrl = MutableLiveData<String?>()
    val releaseYear = MutableLiveData<String>()
    val descriptionText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String?>()

    suspend fun loadDescription() {
        try {
            val film = client.getFilm(movieId)
            title.postValue(film.nameRu ?: "Без названия")
            posterUrl.postValue(film.posterUrl)
            releaseYear.postValue(film.year?.let { "$it" } ?: "Год выхода неизвестен")
            descriptionText.postValue(film.description ?: "Нет описания")
        } catch (e: Exception) {
            errorMessage.postValue(e.toString())
        }
    }
}