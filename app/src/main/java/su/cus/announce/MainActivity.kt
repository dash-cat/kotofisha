package su.cus.announce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import su.cus.announce.DescriptionActivity.DescriptionActivity
import su.cus.announce.databinding.ActivityMainBinding
import su.cus.announce.premiere.PremiereList


interface NavigationController {


    fun openPremiereList()
    fun openDescription( movieId: String)
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
        val fragment = PremiereList(this, this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun openDescription( movieId: String) {
        val intent = Intent(this, DescriptionActivity::class.java)
        intent.putExtra("MOVIE_ID", movieId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "MainActivity view is being destroyed")
    }
}

