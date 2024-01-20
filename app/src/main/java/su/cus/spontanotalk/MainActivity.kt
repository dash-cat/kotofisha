package su.cus.spontanotalk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import su.cus.spontanotalk.databinding.ActivityMainBinding


interface NavigationController {


    fun openPremiereList()

}

interface ISignUpOpener {
    fun openSignUp()
}

class MainActivity : AppCompatActivity(), NavigationController, ISignUpOpener {

    private lateinit var binding: ActivityMainBinding
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

        val id = R.id.nav_host_fragment

//        val fragment = supportFragmentManager.findFragmentById(id) as? NavHostFragment

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.setTitle("Your Custom Title");
        }

//            fragment?.navController?.navigate(R.id.action_to_titleScreen)
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
            MainActivity.RC_SIGN_IN
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MainActivity.RC_SIGN_IN) {
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

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("su.cus.spontanotalk.Login.MainActivity", "su.cus.spontanotalk.Login.MainActivity view is being destroyed")
    }
}

