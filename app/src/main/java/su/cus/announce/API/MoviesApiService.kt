package su.cus.announce.API

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import su.cus.announce.API.FilmById.FilmDataItem
import su.cus.announce.API.MoviesRepository.MoviesPremiere

interface MoviesApiService {
    @GET("movies/{year}/{month}")
    suspend fun getMovies(@Path("year") year: Int, @Path("month") month: String): Response<MoviesPremiere>

    @GET("film/{id}")
    suspend fun getFilmsById(@Path("id") id: String): Response<FilmDataItem>
}


