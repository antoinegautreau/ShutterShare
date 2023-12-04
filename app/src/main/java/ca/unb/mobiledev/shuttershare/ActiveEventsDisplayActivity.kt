package ca.unb.mobiledev.shuttershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.shuttershare.entity.Event
import ca.unb.mobiledev.shuttershare.entity.Folder
import ca.unb.mobiledev.shuttershare.util.ActiveEvents
import ca.unb.mobiledev.shuttershare.util.FinishedEvents

class ActiveEventsDisplayActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var myActiveEventsAdapter: MyActiveEventsAdapter? = null
    private var eventList = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_events_display)

        eventList = ArrayList()

        recyclerView = findViewById<View>(R.id.myActiveEventsList) as RecyclerView
        recyclerView.setHasFixedSize(true)

        myActiveEventsAdapter = MyActiveEventsAdapter(this@ActiveEventsDisplayActivity, eventList) //MyAdapter(this@AlbumActivity, folderList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = myActiveEventsAdapter

        prepareEventListData()

        // Set underline on "Event Name" tag and "Event Code" tag
        val eventNameTag: TextView = findViewById(R.id.eventNameTag)
        val mSpannableString = SpannableString(eventNameTag.text)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        eventNameTag.text = mSpannableString

        val eventCodeTag: TextView = findViewById(R.id.eventCodeTag)
        val mSpannableString2 = SpannableString(eventCodeTag.text)
        mSpannableString2.setSpan(UnderlineSpan(), 0, mSpannableString2.length, 0)
        eventCodeTag.text = mSpannableString2

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            startActivity(Intent(this@ActiveEventsDisplayActivity, MainActivity::class.java))
        }
    }

    fun prepareEventListData(){
        val activeEvents = ActiveEvents()
        val eventNamesList = activeEvents.getArrayOfEventNames(this)
        Log.d("ActiveEventsDisplay", "eventNamesList size: ${eventNamesList.size}")

        for(i in 0..eventNamesList.size-1) {
            var eventName = eventNamesList[i]
            var eventCode = activeEvents.getEventCode(this, eventName!!)

            var event = Event(eventName, eventCode, 0)
            eventList.add(event)
        }

        Log.d("ActiveEventsDisplay", "Event List size: ${eventList.size}")

        myActiveEventsAdapter!!.notifyDataSetChanged()
    }
}