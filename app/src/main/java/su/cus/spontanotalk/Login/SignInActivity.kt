package su.cus.spontanotalk.Login

// import android.app.Activity // No longer handling onActivityResult directly for FirebaseUI
// import android.content.Intent // No longer handling onActivityResult directly for FirebaseUI
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels // For by viewModels()
import androidx.appcompat.app.AppCompatActivity
// FirebaseUI and related FirebaseAuth imports are removed as this activity will now use LoginViewModel
// for custom login/signup, not the FirebaseUI flow directly.
// MainActivity is assumed to handle the FirebaseUI flow if needed.
import su.cus.spontanotalk.Login.ui.login.LoginResult
import su.cus.spontanotalk.Login.ui.login.LoginViewModel
import su.cus.spontanotalk.Login.ui.login.LoginViewModelFactory
import su.cus.spontanotalk.databinding.ActivitySignInBinding // Assuming this layout has email/password fields and buttons

class SignInActivity : AppCompatActivity() {

    // Companion object might not be needed if RC_SIGN_IN is removed.
    // companion object {
    //     private const val RC_SIGN_IN = 123
    // }

    private lateinit var binding: ActivitySignInBinding // Changed to non-nullable assuming it's always inflated

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        loginViewModel.loginResult.observe(this) { loginResult ->
            when (loginResult) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Login Success: ${loginResult.user.displayName}", Toast.LENGTH_SHORT).show()
                    // TODO: Navigate to the main part of the app
                    // Example: startActivity(Intent(this, MainActivity::class.java))
                    // finish()
                }
                is LoginResult.Error -> {
                    Toast.makeText(this, "Login Failed: ${loginResult.error}", Toast.LENGTH_LONG).show()
                }
            }
        }

        loginViewModel.signUpResult.observe(this) { signUpResult ->
            when (signUpResult) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Sign Up Success: ${signUpResult.user.displayName}", Toast.LENGTH_SHORT).show()
                    // TODO: Navigate to the main part of the app or confirm email
                }
                is LoginResult.Error -> {
                    Toast.makeText(this, "Sign Up Failed: ${signUpResult.error}", Toast.LENGTH_LONG).show()
                }
            }
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            // TODO: Show/hide progress bar
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        loginViewModel.loginFormState.observe(this) { formState ->
            if (formState.emailError != null) {
                // binding.emailEditText.error = getString(formState.emailError)
            }
            if (formState.passwordError != null) {
                // binding.passwordEditText.error = getString(formState.passwordError)
            }
        }
    }

    private fun setupClickListeners() {
        // Assuming your ActivitySignInBinding has views like:
        // binding.loginButton.setOnClickListener {
        //     val email = binding.emailEditText.text.toString()
        //     val password = binding.passwordEditText.text.toString()
        //     loginViewModel.login(email, password)
        // }
        //
        // binding.signUpButton.setOnClickListener {
        //     val email = binding.emailEditText.text.toString()
        //     val password = binding.passwordEditText.text.toString()
        //     loginViewModel.signUp(email, password)
        // }
    }

    // Removed createSignInIntent() and onActivityResult() as FirebaseUI flow is
    // assumed to be handled by MainActivity or another dedicated entry point if used.
    // This Activity now focuses on custom UI login/signup with LoginViewModel.
}
