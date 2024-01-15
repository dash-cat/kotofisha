package su.cus.announce.premiere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import su.cus.announce.API.MoviesRepository.Movie
import su.cus.announce.databinding.ListPremiereBinding


class PremiereList() : Fragment(), OnItemsClickListener, PremiereListPresenterOutput {

    private val input: PremiereListPresenterInput by inject { parametersOf(this) }
    private lateinit var binding: ListPremiereBinding
    private val recyclerView by lazy { binding.recyclerView }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListPremiereBinding.inflate(layoutInflater)


        setupRecyclerView()

        CoroutineScope(Dispatchers.IO).launch {
            input.loadMovies()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


    override fun showMovies(moviesList: List<Movie>) {
        recyclerView.adapter = PremiereListAdapter(moviesList, this)
    }


    override fun onItemsClick(movieId: String) {
//        navigationController.openDescription( movieId)
    }

    override suspend fun showErrorMessage(errorMessage: String?) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

}



