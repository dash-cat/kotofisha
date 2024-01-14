package su.cus.announce.API

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import su.cus.announce.API.FilmById.FilmDataItem
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.API.MoviesRepository.MoviesPremiere

interface IRetrofitClient {
    fun getMovies(
        year: Int,
        month: String,
        callback: (Result<List<Movie>>) -> Unit
    )

    fun getFilm(
        id: String,
        callback: (Result<FilmDataItem>) -> Unit
    )
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

    override fun getMovies(year: Int, month: String, callback: (Result<List<Movie>>) -> Unit) {
        client.getMovies(year, month).enqueue(object:
            Callback<MoviesPremiere> {
            override fun onResponse(call: Call<MoviesPremiere>, response: Response<MoviesPremiere>) {
                if (response.isSuccessful) {
                    val moviesList = response.body()?.items ?: emptyList()
                    callback(Result.success(moviesList))

                } else {
                    callback(Result.failure(Exception("Не получилось загрузить")))
                }
            }

            override fun onFailure(call: Call<MoviesPremiere>, t: Throwable) {
                callback(Result.failure(t))
                Log.d("PremiereList", "Network request failed: ${t.message}")
            }
        })
    }

    override fun getFilm(id: String, callback: (Result<FilmDataItem>) -> Unit) {
        client.getFilmsById(id).enqueue(object:
            Callback<FilmDataItem> {
            override fun onResponse(call: Call<FilmDataItem>, response: Response<FilmDataItem>) {
                if (response.isSuccessful) {
                    val film = response.body()
                    if (film != null) {
                        callback(Result.success(film))
                    } else {
                        callback(Result.failure(Exception("Не получилось загрузить")))
                    }
                } else {
                    callback(Result.failure(Exception("Не получилось загрузить")))
                }
            }

            override fun onFailure(call: Call<FilmDataItem>, t: Throwable) {
                callback(Result.failure(t))
                Log.d("PremiereList", "Network request failed: ${t.message}")
            }
        })
    }
}