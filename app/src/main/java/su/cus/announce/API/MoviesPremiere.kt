package su.cus.anonce.API

import su.cus.anonce.API.MoviesRepository.ItemMoviesList

data class MoviesPremiere(
    val items: List<ItemMoviesList>,
    val total: Int
)