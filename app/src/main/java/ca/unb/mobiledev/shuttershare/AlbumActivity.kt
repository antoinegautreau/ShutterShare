package ca.unb.mobiledev.shuttershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.unb.mobiledev.shuttershare.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_album
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home -> {
                    startActivity(Intent(this@AlbumActivity, MainActivity::class.java))
                    overridePendingTransition(0,0)
                    //finish() // Not really sure what this does...
                    true
                }
                R.id.bottom_album -> true
                else -> false
            }
        }
    }
}