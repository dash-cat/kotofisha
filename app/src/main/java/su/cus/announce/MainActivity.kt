package su.cus.announce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import su.cus.announce.databinding.ActivityMainBinding
import su.cus.announce.premiere.PremiereList

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeButton.setOnClickListener {
            val intent = Intent(this, PremiereList::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("MainActivity", "MainActivity view is being destroyed")
    }
}
