package su.cus.spontanotalk.API.MoviesRepository

import kotlinx.serialization.Serializable

@Serializable
data class ListPremiere(
    val items: List<Movie>,
    val total: Int
)