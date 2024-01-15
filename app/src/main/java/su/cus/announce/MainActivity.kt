package su.cus.announce

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import su.cus.announce.databinding.ActivityMainBinding


interface NavigationController {


    fun openPremiereList()

}
class MainActivity : AppCompatActivity(), NavigationController {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val id = R.id.nav_host_fragment

        val fragment = supportFragmentManager.findFragmentById(id) as? NavHostFragment

        if (savedInstanceState == null) {

        }
//            fragment?.navController?.navigate(R.id.action_to_titleScreen)
        }

    override fun openPremiereList() {

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "MainActivity view is being destroyed")
    }
}

