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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import su.cus.spontanotalk.ISignUpOpener
import su.cus.spontanotalk.R
import su.cus.spontanotalk.StarrySkyRenderer
import su.cus.spontanotalk.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private lateinit var glSurfaceView: GLSurfaceView
    private var gso: GoogleSignInOptions? = null
    private val signUpOpener: ISignUpOpener by inject()
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
        initObservers()
    }

    private fun initViews() {
        glSurfaceView = binding.glSurfaceView
        glSurfaceView.setEGLContextClientVersion(2) // Use OpenGL ES 2.0
        glSurfaceView.setRenderer(StarrySkyRenderer())
    }

    private fun initListeners() {
        binding.returnToTitleButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_titleScreen)
        }

        binding.login.setOnClickListener {
            login()
        }

        binding.signInWithGoogle.setOnClickListener {
            signUpOpener.openSignUp()
        }

        binding.textPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    binding.username.text.toString(),
                    binding.textPassword.text.toString()
                )
            }
        }
        binding.username.addTextChangedListener(afterTextChangedListener)
        binding.textPassword.addTextChangedListener(afterTextChangedListener)
    }

    private fun initObservers() {
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        loginViewModel.isSignInButtonEnabled.observe(viewLifecycleOwner) { isSignInButtonEnabled ->
            if (isSignInButtonEnabled) {
                signIn()
            }
        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner) { loginFormState ->
            loginFormState ?: return@observe
            binding.login.isEnabled = loginFormState.isDataValid
            loginFormState.usernameError?.let {
                binding.username.error = getString(it)
            }
            loginFormState.passwordError?.let {
                binding.textPassword.error = getString(it)
            }
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { loginResult ->
            loginResult ?: return@observe
            binding.loading.visibility = View.GONE
            loginResult.error?.let {
                showLoginFailed(it)
            }
            loginResult.success?.let {
                updateUiWithUser(it)
            }
        }
    }

    private fun login() {
        binding.loading.visibility = View.VISIBLE
        lifecycleScope.launch {
            loginViewModel.login(
                binding.username.text.toString(),
                binding.textPassword.text.toString()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        
    }

    private fun signIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(
            requireActivity(),
            gso!!
        )
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