package su.cus.announce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import su.cus.announce.databinding.TitleScreenBinding

class TitleScreen(): Fragment() {

    private lateinit var binding: TitleScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TitleScreenBinding.inflate(inflater, container, false)

        binding. welcomeButton.setOnClickListener {
//            navigationController.openPremiereList()
        }

        return binding.root
    }
}