package su.cus.announce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import su.cus.announce.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding
    private val args: DescriptionFragmentArgs by navArgs()
    private lateinit var viewModel: DescriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedFilm = args.filmDataItem
        println("receivedFilm: $receivedFilm")

        val viewModel: DescriptionViewModel by inject {
            parametersOf(args.filmDataItem.kinopoiskId.toString())
        }

        this.viewModel = viewModel

        viewModel.title.observe(viewLifecycleOwner) { title ->
            binding.nameFilm.text = title
        }

        viewModel.posterUrl.observe(viewLifecycleOwner) { posterUrl ->
            binding.imagePrevFilm
        }

        viewModel.releaseYear.observe(viewLifecycleOwner) { releaseYear ->
            binding.datePrev.text = releaseYear
        }

        viewModel.descriptionText.observe(viewLifecycleOwner) { descriptionText ->
            binding.textDescription.text = descriptionText
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { maybeErrorMessage ->
            maybeErrorMessage?.let { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.loadDescription()
        }

        binding.returnButtonToPremiere.setOnClickListener {
            findNavController().navigate(R.id.action_descriptionFragment_to_premiereList)
        }
    }
}
