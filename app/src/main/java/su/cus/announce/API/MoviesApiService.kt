package su.cus.announce.API

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import su.cus.announce.API.FilmById.FilmDataItem
import su.cus.announce.API.MoviesRepository.ListPremiere

interface MoviesApiService {

    @GET("films/premieres")
    suspend fun getMovies(@Query("year") year: Int, @Query("month") month: String): Response<ListPremiere>

    @GET("films/{id}")
    suspend fun getFilmsById(@Path("id") id: String): Response<FilmDataItem>
}


