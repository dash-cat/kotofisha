package su.cus.announce.API

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import su.cus.announce.API.FilmById.FilmDataItem
import su.cus.announce.API.MoviesRepository.Movie

interface IRetrofitClient {
    @GET("movies")
    suspend fun getMovies(
        @Query("year") year: Int,
        @Query("month") month: String
    ): List<Movie>
    @GET("api/v2.2/films/:id")
    suspend fun getFilm(
        @Query("id") id: String
    ): FilmDataItem
}
class RetrofitClient: IRetrofitClient {
    private val baseUrl = "https://kinopoiskapiunofficial.tech/"
    private val apiKey = "0161a006-38d5-448a-9ce4-9c72e615dbf5"
    private val client: MoviesApiService

    init {
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-API-KEY", apiKey)
                .addHeader("accept", "application/json")
                .build()
            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        client = retrofit.create(MoviesApiService::class.java)
    }

    override suspend fun getMovies(year: Int, month: String): List<Movie> {

        val response = client.getMovies(year, month)
        println("RESP $response")
            if (response.isSuccessful) {
                return response.body()?.items ?: emptyList()
            } else {
                throw Exception("Не получилось загрузить")
            }
    }

    override suspend fun getFilm(id: String): FilmDataItem {
            val response = client.getFilmsById(id)
            if (response.isSuccessful) {
                return response.body()
                    ?: throw Exception("Не получилось загрузить")
            } else {
                throw Exception("Не получилось загрузить")
            }
    }
}