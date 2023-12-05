package ca.unb.mobiledev.shuttershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// possibly don't need these two
import ca.unb.mobiledev.shuttershare.entity.Folder
import ca.unb.mobiledev.shuttershare.util.FinishedEvents
import org.w3c.dom.Text

class AlbumActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var myFolderAdapter: MyFolderAdapter? = null
    private var folderList = mutableListOf<Folder>()

    //TODO put in picture activity???
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        folderList = ArrayList()

        recyclerView = findViewById<View>(R.id.myFolderList) as RecyclerView
        myFolderAdapter = MyFolderAdapter(this@AlbumActivity, folderList)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = myFolderAdapter

        prepareFolderListData()


        //Bottom Nav
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

    private fun prepareFolderListData() {
        val finishedEvents = FinishedEvents()
        val eventNamesList = finishedEvents.getArrayOfEventNames(this)
        val eventCodesList = finishedEvents.getArrayOfEventCodes(this)

        for(i in 0..eventNamesList.size-1) {
            var folder = Folder(eventNamesList[i]!!, eventCodesList[i]!!, R.drawable.baseline_folder_open_24)
            folderList.add(folder)
        }

        if(!folderList.isEmpty()){
            val noFoldersFoundText: TextView = findViewById(R.id.noFoldersFoundText)
            noFoldersFoundText.visibility = View.GONE
        }
//        var folder = Folder("Avatar", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Batman", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//
//        folder = Folder("End Game", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Hulk", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Inception", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Jumanji", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Lucy", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Jurassic World", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Spider Man", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)
//        folder = Folder("Venom", R.drawable.baseline_folder_open_24)
//        folderList.add(folder)

        myFolderAdapter!!.notifyDataSetChanged()

        //All Code is done let's run the app
    }

    // TODO put this in picture activity??

//        // currently test images
//        val images = listOf(
//            ImageModel(R.drawable.baseline_photo_camera_24),
//            ImageModel(R.drawable.ic_launcher_foreground)
//            //More images if needed
//        )
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = GridLayoutManager(this, 2) //2 colums
//        imageAdapter = ImageAdapter(images)
//        recyclerView.adapter = imageAdapter

//    fun onPhotoClick(view: View) {
//
//        val imageView = view as ImageView
//
//        imageView.layoutParams = RelativeLayout.LayoutParams(
//            RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.MATCH_PARENT
//        )
//
//        //Enable Zoom in/out gestures
//        imageView.setOnTouchListener { _, event ->
//            val scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(imageView))
//            scaleGestureDetector.onTouchEvent(event)
//            true
//        }
//    }
}

// TODO put this in picture activity???
//private class ScaleListener(private val imageView: ImageView) :
//    ScaleGestureDetector.SimpleOnScaleGestureListener() {
//
//    override fun onScale(detector: ScaleGestureDetector): Boolean {
//        val scaleFactor = detector.scaleFactor
//        imageView.scaleX *= scaleFactor
//        imageView.scaleY *= scaleFactor
//        return true
//
//    }
//
//}