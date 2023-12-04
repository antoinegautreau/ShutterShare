package ca.unb.mobiledev.shuttershare

import android.os.Bundle
import android.util.Log
import android.view.View
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
    //private lateinit var extras: ExtendedDataHolder

    //private lateinit val storage: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pictures)

        pictureList = ArrayList()

        recyclerView = findViewById<View>(R.id.myPictureList) as RecyclerView
        myPictureAdapter = MyPictureAdapter(this@PicturesActivity, pictureList)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = myPictureAdapter

        Log.d("PictureActivity", "in picture activity")

        //extras = ExtendedDataHolder.instance


        preparePictureListData()
    }

    private fun preparePictureListData() {
    // start maybe good ---------------------------------------
        val extras = intent
        if(intent.hasExtra("EventName")){//extras.hasExtra("EventName")) {
            Log.d("PictureActivity", "has extra")
            val finishedEvents = FinishedEvents()
            val eventName = extras.getStringExtra("EventName") as String
            val eventCode = finishedEvents.getEventCode(this, eventName)

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
                        pictureList.add(Picture(it.toString()))
                    }.addOnCompleteListener {
                        //???
                        Log.d("PictureActivity", "we are completing...")
//                        recyclerView!!.adapter = MyPictureAdapter(this, pictureList)
//                        recyclerView!!.layoutManager = LinearLayoutManager(this)
                        Log.d("PictureActivity", "pictureList.size: " + pictureList.size)
                        myPictureAdapter!!.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Log.d("PictureActivity", "we are getting errors...")
                    }
                }
            }



//            for(picture in pictureList){
//                Log.d("PictureActivity", "imageURL: " + picture.imageUrl)
//            }


// end maybe good ------ DONT FORGET THE BRACKET FOR IF AT THE BOTTOM WHERE THE ELSE IS ---------------------------------


//            storage.listAll().addOnSuccessListener(OnSuccessListener {
//                onSuc
//            })
//
//
//            for (i in 0..pictureNameList.size - 1) {
//                var picture = Picture()
//                pictureList.add(picture)
//            }

            //From Firebase Docs
//            storage.listAll()
//                .addOnSuccessListener { (items, prefixes) ->
//                    for (prefix in prefixes) {
//                        Log.d("PictureActivity", "Prefix name: " + prefix.name)
//                        // All the prefixes under listRef.
//                        // You may call listAll() recursively on them.
//                    }
//
//                    for (item in items) {
//                        Log.d("PictureActivity", "Item name: " + item.name)
//                        // All the items under listRef.
//                    }
//                }
//                .addOnFailureListener {
//                    // Uh-oh, an error occurred!
//                }

        //THESE GUYS
        }
        else {
            // go back??
            Log.d("PictureActivity", "doesn't have extra??")
        }
    }
}
