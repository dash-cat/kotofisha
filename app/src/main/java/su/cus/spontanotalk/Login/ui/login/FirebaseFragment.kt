package su.cus.spontanotalk.Login.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import su.cus.spontanotalk.databinding.FragmentFirebaseBinding

class FirebaseFragment : Fragment() {
    private lateinit var binding: FragmentFirebaseBinding
    companion object {
        fun newInstance() = FirebaseFragment()
    }

    private val viewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFirebaseBinding.inflate(layoutInflater)
        return binding.root
    }
}