package su.cus.announce.premiere

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import su.cus.announce.R
import su.cus.announce.databinding.ListPremiereBinding
import su.cus.anonce.API.MoviesApiService
import su.cus.anonce.API.MoviesRepository.ItemMoviesList
import su.cus.anonce.API.RetrofitClient
import su.cus.anonce.premiere.PremiereListAdapter

class PremiereList : ComponentActivity() {

//    private lateinit var apiService: MoviesApiService
    private lateinit var binding: ListPremiereBinding
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListPremiereBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadMovies()
    }
    private fun loadMovies() {
        RetrofitClient.instance.getMovies(2023, "january").enqueue(object :
            Callback<List<ItemMoviesList>> {
            override fun onResponse(call: Call<List<ItemMoviesList>>, response: Response<List<ItemMoviesList>>) {
                println("Hi")
                if (response.isSuccessful) {
                    // Теперь у нас есть List<ItemMoviesList>, который мы можем передать в адаптер
                    val moviesList = response.body() ?: emptyList()
                    recyclerView.adapter = PremiereListAdapter(moviesList)
                } else {
                    Toast.makeText(this@PremiereList, "Failed to load movies:", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemMoviesList>>, t: Throwable) {
                Toast.makeText(this@PremiereList, "Failed to load movies: $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



