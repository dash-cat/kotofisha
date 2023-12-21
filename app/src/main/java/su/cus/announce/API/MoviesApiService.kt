package su.cus.announce.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import su.cus.announce.API.FilmById.FilmDataItem
import su.cus.announce.API.MoviesRepository.MoviesPremiere

interface MoviesApiService {
    @GET("api/v2.2/films/premieres")
    fun getMovies(
        @Query("year") year: Int,
        @Query("month") month: String
    ): Call<MoviesPremiere>

    @GET("api/v2.2/films/{id}")
    fun getFilmsById(
        @Path("id") id: String
    ): Call<FilmDataItem>
}



