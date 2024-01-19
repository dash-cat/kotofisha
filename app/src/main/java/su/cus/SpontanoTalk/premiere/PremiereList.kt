package su.cus.SpontanoTalk.premiere

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
import su.cus.SpontanoTalk.API.FilmDescription.FilmDataItem
import su.cus.SpontanoTalk.API.MoviesRepository.ListPremiere
import su.cus.SpontanoTalk.databinding.ListPremiereBinding


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

        recyclerView.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            input.loadMovies()
        }

        return binding.root
    }


    override fun showMovies(premiere: ListPremiere) {
            if (premiere.items.isNotEmpty()) {
                recyclerView.adapter = PremiereListAdapter(premiere.items, this)
            } else {
                println("Movies list is empty")
            }
    }



    override fun onItemsClick(movieId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val film = input.getMovieById(movieId)
            withContext(Dispatchers.Main) {
                film?.let {
                    sendFilmToDescription(it)
                } ?: run {
                    Toast.makeText(context, "Film not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

     private fun sendFilmToDescription(film: FilmDataItem) {
        println("FIlm $film")
        val action = PremiereListDirections.actionPremiereListToDescriptionFragment(film)
        findNavController().navigate(action)
    }



    override suspend fun showErrorMessage(errorMessage: String?) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}



