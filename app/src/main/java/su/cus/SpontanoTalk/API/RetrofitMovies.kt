package su.cus.SpontanoTalk.API

import su.cus.SpontanoTalk.API.FilmDescription.FilmDataItem
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import su.cus.SpontanoTalk.API.MoviesRepository.ListPremiere

interface IRetrofitClient {

    suspend fun getMovies(
       year: Int,
       month: String
    ): ListPremiere

    suspend fun getFilm(
        id: String
    ): FilmDataItem
}
class RetrofitClient: IRetrofitClient {
    private val baseUrl = "https://kinopoiskapiunofficial.tech/api/v2.2/"
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

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        client = retrofit.create(MoviesApiService::class.java)
    }

    override suspend fun getMovies(year: Int, month: String): ListPremiere {
        println("TuT")
        val response = client.getMovies(year, month)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
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