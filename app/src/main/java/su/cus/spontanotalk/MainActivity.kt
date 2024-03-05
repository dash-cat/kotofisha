package su.cus.spontanotalk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


interface NavigationController {
    fun openPremiereList()

}

interface ISignUpOpener {
    fun openSignUp()
}

class MainActivity : AppCompatActivity(), NavigationController, ISignUpOpener {

    companion object {
        private const val RC_SIGN_IN = 123
        var instance: MainActivity? = null
            private set
    }

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        // Инициализация NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Навигация назад с использованием NavController
                navController.navigateUp()
                Log.d("MyActivity", "Back button pressed")
                println("Worked")
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun openSignUp() {
        createSignInIntent()
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
            // Добавьте другие провайдеры при необходимости
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                // Обрабатываем успешный вход пользователя
            } else {
                // Обработка ошибок входа
                if (response == null) {
                    // Пользователь нажал кнопку "назад"
                    return
                }
                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    // Ошибка сети
                    return
                }
                // Другие ошибки
            }
        }
    }


    override fun openPremiereList() {

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("su.cus.spontanotalk.Login.MainActivity", "su.cus.spontanotalk.Login.MainActivity view is being destroyed")
    }
}
