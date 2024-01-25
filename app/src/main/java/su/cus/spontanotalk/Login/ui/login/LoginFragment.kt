package su.cus.spontanotalk.Login.ui.login

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.android.ext.android.inject
import su.cus.spontanotalk.ISignUpOpener
import su.cus.spontanotalk.R
import su.cus.spontanotalk.StarrySkyRenderer
import su.cus.spontanotalk.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
    }
    private var _binding: FragmentLoginBinding? = null
    private lateinit var glSurfaceView: GLSurfaceView
    private val signUpOpener: ISignUpOpener by inject()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding get() = _binding!!

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
        initViews()
        initListeners()
//        initObservers()
        initGoogleSignInClient()
    }
    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }
    private fun initViews() {
        glSurfaceView = binding.glSurfaceView
        glSurfaceView.setEGLContextClientVersion(2) // Use OpenGL ES 2.0
        glSurfaceView.setRenderer(StarrySkyRenderer())
    }

    private fun initListeners() {
        binding.apply {
            returnToTitleButton.setOnClickListener {
                println("tututut")
                findNavController().navigate(R.id.action_loginFragment_to_titleFragment)
            }
            login.setOnClickListener {  signIn() }
            signInWithGoogle.setOnClickListener { signUpOpener.openSignUp() }
            textPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signIn()
                }
                false
            }
//            val afterTextChangedListener = TextWatcherAfter { updateLoginData() }
//            username.addTextChangedListener(afterTextChangedListener)
//            textPassword.addTextChangedListener(afterTextChangedListener)
        }
    }
    private fun TextWatcherAfter(afterTextChanged: (Editable?) -> Unit) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) = afterTextChanged(s)
    }
//    private fun initObservers() {
//        loginViewModel.isSignInButtonEnabled.observe(viewLifecycleOwner) { isSignInButtonEnabled ->
//            if (isSignInButtonEnabled) {
//                signIn()
//            }
//        }
//
//        loginViewModel.loginFormState.observe(viewLifecycleOwner) { loginFormState ->
//            loginFormState ?: return@observe
//            binding.login.isEnabled = loginFormState.isDataValid
//            loginFormState.usernameError?.let {
//                binding.username.error = getString(it)
//            }
//            loginFormState.passwordError?.let {
//                binding.textPassword.error = getString(it)
//            }
//        }
//
//        loginViewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
//            loginResult ?: return@observe
//            binding.loading.visibility = View.GONE
//            loginResult.error?.let {
//                showLoginFailed(it)
//            }
//            loginResult.success?.let {
//                updateUiWithUser(it)
//            }
//        }
//    }

//    private fun login() {
//        binding.loading.visibility = View.VISIBLE
//        lifecycleScope.launch {
//            loginViewModel.login(
//                binding.username.text.toString(),
//                binding.textPassword.text.toString()
//            )
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//
//    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}