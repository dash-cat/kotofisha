package su.cus.spontanotalk.Login.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.android.ext.android.inject
import su.cus.spontanotalk.ISignUpOpener
import su.cus.spontanotalk.R
import su.cus.spontanotalk.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    var onLoading: (() -> Unit)? = null
    var onSuccess: ((String) -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null



    private var _binding: FragmentLoginBinding? = null
//    private lateinit var glSurfaceView: GLSurfaceView
    private val signUpOpener: ISignUpOpener by inject()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding get() = _binding!!
    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d("LoginFragment", "onViewCreated")
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                Log.d("LoginFragment", "Back pressed in LoginFragment")
//                findNavController().navigate(R.id.action_loginFragment_to_titleFragment)
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//        initViews()
        initListeners()
        initGoogleSignInClient()
    }



    private fun setupViewModel() {
        loginViewModel.onLoading = {
            // Показать индикатор загрузки, например ProgressBar
        }

        loginViewModel.onSuccess = { data ->
            // Обновить интерфейс данными, например, TextView или RecyclerView
        }

        loginViewModel.onError = { error ->
            // Показать сообщение об ошибке, например, через Toast или Snackbar
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очищаем замыкания, чтобы избежать утечек памяти
        loginViewModel.onLoading = null
        loginViewModel.onSuccess = null
        loginViewModel.onError = null
    }
    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

//    private fun initViews() {
//        glSurfaceView = binding.glSurfaceView
//        glSurfaceView.setEGLContextClientVersion(2) // Use OpenGL ES 2.0
//        glSurfaceView.setRenderer(StarrySkyRenderer())
//    }


    private fun initListeners() {
        binding.apply {
            returnToTitleButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_titleFragment)
            }
            login.setOnClickListener { signIn() }
            signInWithGoogle.setOnClickListener { signUpOpener.openSignUp() }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

}
