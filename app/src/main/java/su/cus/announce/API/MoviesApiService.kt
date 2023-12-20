package su.cus.announce.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import su.cus.announce.API.MoviesRepository.ItemMoviesList

interface MoviesApiService {
    @GET("api/v2.2/films/premieres")
    fun getMovies(
        @Query("year") year: Int,
        @Query("month") month: String
    ): Call<MoviesPremiere>
}



