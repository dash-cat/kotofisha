package su.cus.announce.premiere

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import su.cus.announce.API.MoviesRepository.ListPremiere
import su.cus.announce.databinding.ListPremiereBinding


class PremiereList() : Fragment(), OnItemsClickListener, PremiereListPresenterOutput {

    private val input: PremiereListPresenterInput by inject { parametersOf(this) }
    private lateinit var binding: ListPremiereBinding
    private val recyclerView by lazy { binding.recyclerView }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListPremiereBinding.inflate(layoutInflater)


        setupRecyclerView()

        CoroutineScope(Dispatchers.IO).launch {
            input.loadMovies()
        }

        return binding.root
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)

    }


    override fun showMovies(premiere: ListPremiere) {
        println("moviesList $premiere")

            if (premiere.items.isNotEmpty()) {
                recyclerView.adapter = PremiereListAdapter(premiere.items, this)
            } else {
                println("Movies list is empty")
            }

    }


    override fun onItemsClick(movieId: String) {

        val action = PremiereListDirections.actionPremiereListToDescriptionFragment(movieId)
        findNavController().navigate(action)

        CoroutineScope(Dispatchers.IO).launch {
            input.getMovieById(movieId)
        }

    }


    override suspend fun showErrorMessage(errorMessage: String?) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

}



