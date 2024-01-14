package su.cus.announce.premiere

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.API.PremiereListPresenterInput
import su.cus.announce.API.PremiereListPresenterOutput
import su.cus.announce.NavigationController
import su.cus.announce.databinding.ListPremiereBinding


class PremiereList(
    private val navigationController: NavigationController,
    private val context: Context
) : Fragment(), OnItemsClickListener, PremiereListPresenterOutput {

    private val input: PremiereListPresenterInput by inject()
    private lateinit var binding: ListPremiereBinding
    private val recyclerView by lazy { binding.recyclerView }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListPremiereBinding.inflate(layoutInflater)


        setupRecyclerView()
        input.loadMovies()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


    override fun showMovies(moviesList: List<Movie>) {
        recyclerView.adapter = PremiereListAdapter(moviesList, this@PremiereList)
    }


    override fun onItemsClick(movieId: String) {
        navigationController.openDescription( movieId)
    }

    override fun showErrorMessage(errorMessage: String?) {
        Toast.makeText(context, "Failed to load movies: $errorMessage", Toast.LENGTH_SHORT).show()
    }

}



