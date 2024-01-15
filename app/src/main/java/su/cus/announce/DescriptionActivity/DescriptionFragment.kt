package su.cus.announce.DescriptionActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import su.cus.announce.R
import su.cus.announce.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DescriptionFragmentArgs.fromBundle(requireArguments())
        val receivedData = args.movieId

        binding.returnButtonToPremiere.setOnClickListener {
            println("Button clicked")
            val navController = findNavController()
            println("NavController: $navController")
            navController.navigate(R.id.action_descriptionFragment_to_premiereList)
        }
    }
}
