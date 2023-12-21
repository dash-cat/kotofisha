package su.cus.announce.API.MoviesRepository

import kotlinx.serialization.Serializable

@Serializable
data class MoviesPremiere(
    val items: List<ItemMoviesList>,
    val total: Int
)