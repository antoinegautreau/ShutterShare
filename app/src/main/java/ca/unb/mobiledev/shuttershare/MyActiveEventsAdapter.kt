package ca.unb.mobiledev.shuttershare

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.shuttershare.entity.Event

class MyActiveEventsAdapter constructor(
    private val getActivity: Activity,
    private val eventList: List<Event>) :
    RecyclerView.Adapter<MyActiveEventsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.active_event_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvEventName.text = eventList[position].name
        holder.tvEventCode.text = eventList[position].code

//        holder.tvFolderTitle.text = folderList[position].title
//        holder.ivFolderImg.setImageResource(folderList[position].image)
//
//        holder.cardView.setOnClickListener {
//            val intent = Intent(getActivity, PicturesActivity::class.java)
//            intent.putExtra("EventName", folderList[position].title)
//            getActivity.startActivity(intent)
//
//            Toast.makeText(getActivity, folderList[position].title, Toast.LENGTH_SHORT).show()
//        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventName: TextView = itemView.findViewById(R.id.EventNameText)
        val tvEventCode: TextView = itemView.findViewById(R.id.eventCodeText)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

}