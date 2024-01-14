package su.cus.announce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import su.cus.announce.databinding.TitleScreenBinding

class TitleScreen(): Fragment() {

    private lateinit var binding: TitleScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TitleScreenBinding.inflate(inflater, container, false)
        println("noteswnoti")



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.welcomeButton.setOnClickListener {
            println("noteswnoti")
            findNavController().navigate(R.id.action_titleScreen_to_premiereList)
        }
    }
}