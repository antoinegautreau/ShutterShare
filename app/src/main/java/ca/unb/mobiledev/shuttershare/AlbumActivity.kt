package ca.unb.mobiledev.shuttershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.unb.mobiledev.shuttershare.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)


        // currently test images
        val images = listOf(
            ImageModel(R.drawable.baseline_photo_camera_24),
            ImageModel(R.drawable.ic_launcher_foreground)
            //More images if needed
        )
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) //2 colums
        imageAdapter = ImageAdapter(images)
        recyclerView.adapter = imageAdapter


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