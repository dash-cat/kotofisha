package su.cus.announce.API.MoviesRepository

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val genre: String
)