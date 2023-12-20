package su.cus.announce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import su.cus.announce.databinding.ActivityMainBinding
import su.cus.announce.premiere.PremiereList

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val button: Button? = findViewById(R.id.welcome_button)
        println("$button")
        button?.setOnClickListener(){

            val intent = Intent(/* packageContext = */ this, /* cls = */ PremiereList::class.java)
            println("$intent")
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "MainActivity is being destroyed")
    }

}
