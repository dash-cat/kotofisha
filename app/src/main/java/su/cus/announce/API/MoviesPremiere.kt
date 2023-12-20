package su.cus.announce.API

import kotlinx.serialization.Serializable
import su.cus.announce.API.MoviesRepository.ItemMoviesList
@Serializable
data class MoviesPremiere(
    val items: List<ItemMoviesList>,
    val total: Int
)