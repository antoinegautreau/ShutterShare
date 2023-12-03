package ca.unb.mobiledev.shuttershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager.widget.ViewPager
import ca.unb.mobiledev.shuttershare.databinding.ActivityJoinCreateEventBinding
import ca.unb.mobiledev.shuttershare.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference

class JoinCreateEventActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityJoinCreateEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_create_event)
        viewBinding = ActivityJoinCreateEventBinding.inflate(layoutInflater)

        val tabLayout: TabLayout
        val viewPager: ViewPager
        val viewPagerAdapter: ViewPagerAdapter

        //TabLayout stuff
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            //finish()
            //overridePendingTransition(0,0)
        }
    }
}