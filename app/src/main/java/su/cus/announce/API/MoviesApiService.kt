package su.cus.anonce.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import su.cus.anonce.API.MoviesRepository.ItemMoviesList
import java.nio.channels.NetworkChannel

interface MoviesApiService {
    @GET("api/v2.2/films/premieres")
    fun getMovies(
        @Query("year") year: Int,
        @Query("month") month: String
    ): Call<List<ItemMoviesList>>
}


//class MoviesApiServiceFactory {
//    fun create(): MoviesApiService{
//        val networkClient = NetworkClient()
//        return MoviesApiService(networkClient)
//    }
//}


