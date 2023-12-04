package ca.unb.mobiledev.shuttershare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.shuttershare.entity.Picture
import ca.unb.mobiledev.shuttershare.util.FinishedEvents
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference


class PicturesActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var myPictureAdapter: MyPictureAdapter? = null
    private var pictureList = mutableListOf<Picture>()
    private var eventName: String? = ""
    //private lateinit var extras: ExtendedDataHolder

    //private lateinit val storage: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pictures)

        if(intent.hasExtra("EventName")) {
            eventName = intent.getStringExtra("EventName")
            Log.d("PictureActivity", "has EventName...")
        }

        pictureList = ArrayList()

        recyclerView = findViewById<View>(R.id.myPictureList) as RecyclerView
        myPictureAdapter = MyPictureAdapter(this@PicturesActivity, pictureList)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = myPictureAdapter

        // Get Images and set into recyclerView
        preparePictureListData()

        // Set Title
        val eventTitle: TextView = findViewById(R.id.eventTitle)
        eventTitle.text = eventName

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this@PicturesActivity, AlbumActivity::class.java))
        }
    }

    private fun preparePictureListData() {
        if(intent.hasExtra("EventName")){
            Log.d("PictureActivity", "has extra")
            val finishedEvents = FinishedEvents()
            //val eventName = extras.getStringExtra("EventName") as String
            val eventCode = finishedEvents.getEventCode(this, eventName!!)

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child(eventCode)

            val listAllTasks: Task<ListResult> = storageRef.listAll()
            Log.d("PictureActivity", "${listAllTasks.toString()}")
            listAllTasks.addOnCompleteListener { result ->
                val items: List<StorageReference> = result.result!!.items
                //add cycle for add image url to list
                items.forEachIndexed { index, item ->
                    item.downloadUrl.addOnSuccessListener {
                        Log.d("PictureActivity", "${it.toString()}")
                        pictureList.add(Picture(it.toString(), eventName!!))
                    }.addOnCompleteListener {
                        Log.d("PictureActivity", "we are completing...")
                        Log.d("PictureActivity", "pictureList.size: " + pictureList.size)
                        myPictureAdapter!!.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Log.d("PictureActivity", "we are getting errors...")
                    }
                }
            }
        }
        else {
            // go back??
            Log.d("PictureActivity", "doesn't have extra??")
        }
    }
}
