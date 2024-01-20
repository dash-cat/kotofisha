//package su.cus.spontanotalk.Login
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.firebase.ui.auth.AuthUI
//import com.firebase.ui.auth.ErrorCodes
//import com.firebase.ui.auth.IdpResponse
//import com.google.firebase.auth.FirebaseAuth
//import su.cus.spontanotalk.databinding.ActivitySignInBinding
//
//class SignInActivity : AppCompatActivity() {
//
//    companion object {
//        private const val RC_SIGN_IN = 123
//    }
//
//    private lateinit var _binding: ActivitySignInBinding
//
//
//    private val binding get() = _binding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        _binding = ActivitySignInBinding.inflate(layoutInflater)
//        setContentView(_binding.root)
//
//        // Here you can set up listeners or initialize UI components using the binding
//    }
//
//    private fun createSignInIntent() {
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build()
//            // Добавьте другие провайдеры при необходимости
//        )
//
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(),
//            RC_SIGN_IN
//        )
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
//
//            if (resultCode == Activity.RESULT_OK) {
//                val user = FirebaseAuth.getInstance().currentUser
//                // Обрабатываем успешный вход пользователя
//            } else {
//                // Обработка ошибок входа
//                if (response == null) {
//                    // Пользователь нажал кнопку "назад"
//                    return
//                }
//                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
//                    // Ошибка сети
//                    return
//                }
//                // Другие ошибки
//            }
//        }
//    }
//
//    // ... Остальная часть вашего кода ...
//}
