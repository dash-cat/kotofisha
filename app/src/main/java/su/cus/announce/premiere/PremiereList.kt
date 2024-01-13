package su.cus.announce.premiere

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.API.PremiereListPresenterInput
import su.cus.announce.API.PremiereListPresenterOutput
import su.cus.announce.DescriptionActivity.DescriptionActivity
import su.cus.announce.databinding.ListPremiereBinding


class PremiereList : ComponentActivity(), OnItemsClickListener, PremiereListPresenterOutput {

    private val input: PremiereListPresenterInput by inject()
    private lateinit var binding: ListPremiereBinding
    private val recyclerView by lazy { binding.recyclerView }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListPremiereBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        input.loadMovies()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    override fun showMovies(moviesList: List<Movie>) {
        recyclerView.adapter = PremiereListAdapter(moviesList, this@PremiereList)
    }


    override fun onItemsClick(movieId: String) {
        val intent = Intent(this, DescriptionActivity::class.java)
        intent.putExtra("MOVIE_ID", movieId)
        startActivity(intent)
    }

    override fun showErrorMessage(errorMessage: String?) {
        Toast.makeText(this, "Failed to load movies: $errorMessage", Toast.LENGTH_SHORT).show()
    }

}



