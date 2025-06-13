package su.cus.spontanotalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import su.cus.spontanotalk.databinding.FragmentDescriptionBinding
import android.util.Log // For logging

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null // Use nullable backing property
    private val binding get() = _binding!! // Non-null accessor

    private val args: DescriptionFragmentArgs by navArgs()
    private val viewModel: DescriptionViewModel by inject {
        parametersOf(args.filmDataItem.kinopoiskId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false) // Inflate with parent
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // args.filmDataItem is available here if needed directly, but kinopoiskId is passed to VM.
        // Log.d("DescriptionFragment", "Received film ID: ${args.filmDataItem.kinopoiskId}")

        setupUiAndObservers()
        viewModel.loadDescription() // Call non-suspend function
    }

    private fun setupUiAndObservers() {
        val ratingBar: RatingBar = binding.kinopoiskRaiting
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        ratingBar.startAnimation(fadeInAnimation)

        viewModel.ratingKinopoisk.observe(viewLifecycleOwner) { rangeCountKinopoisk ->
            // Handle null safely and provide a default (e.g., 0f)
            binding.kinopoiskRaiting.rating = rangeCountKinopoisk?.toFloat() ?: 0f
        }

        // The imdbRating LiveData is available in ViewModel if a separate UI element is added for it.
        // For now, only kinopoiskRaiting is displayed on the primary rating bar.

        viewModel.title.observe(viewLifecycleOwner) { title ->
            binding.nameFilm.text = title
        }

        viewModel.posterUrl.observe(viewLifecycleOwner) { posterUrl ->
            Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background) // Optional: add a placeholder
                .error(R.drawable.ic_launcher_foreground) // Optional: add an error image
                .into(binding.imagePrevFilm)
        }

        viewModel.releaseYear.observe(viewLifecycleOwner) { releaseYear ->
            binding.datePrev.text = releaseYear
        }

        viewModel.genreText.observe(viewLifecycleOwner) { genreText ->
            binding.genreTitle.text = genreText
        }

        viewModel.descriptionText.observe(viewLifecycleOwner) { descriptionText ->
            binding.textDescription.text = descriptionText
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { maybeErrorMessage ->
            maybeErrorMessage?.let { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show() // Use LONG for better visibility
                // Potentially clear the error message in VM after shown, or have a dismiss mechanism
            }
        }

        binding.returnButtonToPremiere.setOnClickListener {
            findNavController().navigate(R.id.action_descriptionFragment_to_premiereList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding to avoid memory leaks
    }
}
