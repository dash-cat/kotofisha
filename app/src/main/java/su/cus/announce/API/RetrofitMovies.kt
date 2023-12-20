package su.cus.announce.API

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech/"
    private const val API_KEY = "0161a006-38d5-448a-9ce4-9c72e615dbf5" // Замените на ваш ключ API

    val instance: MoviesApiService by lazy {
        // Создание Interceptor для добавления заголовков
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-API-KEY", API_KEY)
                .addHeader("accept", "application/json")// Добавление заголовка
                .build()
            chain.proceed(request)
        }

        // Создание OkHttpClient и добавление Interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        // Построение Retrofit с OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(MoviesApiService::class.java)
    }
}