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
        // Removed: var instance: MainActivity? = null
    }

    // Removed: init block that set static instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Инициализация NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("MainActivity", "Back button pressed, attempting to navigate up.")
                // Navigate up with NavController. Only finish if navigateUp() returns false (can't navigate up).
                if (!navController.navigateUp()) {
                    Log.d("MainActivity", "Navigated up to the start of the graph, finishing activity.")
                    finish()
                } else {
                    Log.d("MainActivity", "Successfully navigated up.")
                }
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
        // This is part of NavigationController interface.
        // If MainActivity is not supposed to handle this, it should be logged or throw an exception.
        // For now, making it a no-op.
        Log.d("MainActivity", "openPremiereList called, but no implementation is present.")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("su.cus.spontanotalk.Login.MainActivity", "su.cus.spontanotalk.Login.MainActivity view is being destroyed")
    }
}

