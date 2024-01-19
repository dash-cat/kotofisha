package su.cus.SpontanoTalk.API.MoviesRepository

import kotlinx.serialization.Serializable

@Serializable
data class ListPremiere(
    val items: List<Movie>,
    val total: Int
)