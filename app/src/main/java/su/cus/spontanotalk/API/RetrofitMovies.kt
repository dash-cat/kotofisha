package su.cus.spontanotalk.API

import su.cus.spontanotalk.API.FilmDescription.FilmDataItem
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import su.cus.spontanotalk.API.MoviesRepository.ListPremiere

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
        try {
            val response = client.getMovies(year, month)
            if (response.isSuccessful) {
                return response.body()
                    ?: throw ApiException.UnknownApiException("Empty response body when fetching movies")
            } else {
                throw when (response.code()) {
                    401 -> ApiException.UnauthorizedException("Unauthorized access when fetching movies: ${response.message()}")
                    404 -> ApiException.NotFoundException("Movies endpoint not found: ${response.message()}")
                    503 -> ApiException.ServiceUnavailableException("Service unavailable when fetching movies: ${response.message()}")
                    else -> ApiException.UnexpectedCodeException("Unexpected API error when fetching movies: ${response.code()} ${response.message()}", response.code())
                }
            }
        } catch (e: IOException) {
            throw ApiException.NetworkException("Network error when fetching movies: ${e.message}", e)
        } catch (e: Exception) {
            // Catch any other unexpected exceptions
            throw ApiException.UnknownApiException("Unknown error when fetching movies: ${e.message}", e)
        }
    }

    override suspend fun getFilm(id: String): FilmDataItem {
        try {
            val response = client.getFilmsById(id)
            if (response.isSuccessful) {
                return response.body()
                    ?: throw ApiException.UnknownApiException("Empty response body when fetching film with id: $id")
            } else {
                throw when (response.code()) {
                    401 -> ApiException.UnauthorizedException("Unauthorized access when fetching film id $id: ${response.message()}")
                    404 -> ApiException.NotFoundException("Film with id $id not found: ${response.message()}")
                    503 -> ApiException.ServiceUnavailableException("Service unavailable when fetching film id $id: ${response.message()}")
                    else -> ApiException.UnexpectedCodeException("Unexpected API error when fetching film id $id: ${response.code()} ${response.message()}", response.code())
                }
            }
        } catch (e: IOException) {
            throw ApiException.NetworkException("Network error when fetching film id $id: ${e.message}", e)
        } catch (e: Exception) {
            // Catch any other unexpected exceptions
            throw ApiException.UnknownApiException("Unknown error when fetching film id $id: ${e.message}", e)
        }
    }
}