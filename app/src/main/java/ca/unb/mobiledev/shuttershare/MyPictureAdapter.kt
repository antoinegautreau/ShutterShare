package ca.unb.mobiledev.shuttershare

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.shuttershare.entity.Picture
import com.squareup.picasso.Picasso

class MyPictureAdapter constructor(
    private val getActivity: Activity,
    private val pictureList: List<Picture>) :
    RecyclerView.Adapter<MyPictureAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context) // or getActivity??
            .inflate(R.layout.picture_item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture = pictureList[position]
        Picasso.get().load(picture.imageUrl).into(holder.ivPictureImg)

        holder.cardView.setOnClickListener {
            val intent = Intent(getActivity, PictureDetailActivity::class.java)
            intent.putExtra("ImageURL", picture.imageUrl)
            intent.putExtra("EventName", picture.eventName)

            Log.d("MyPictureAdapterLog", "${picture.eventName} ${picture.imageUrl}")
            getActivity.startActivity(intent)
        }
        //holder.ivPictureImg.rotation = -90f
//        holder.ivPictureImg.setImageResource(pictureList[position].image)
//
//        holder.cardView.setOnClickListener {
//            // TODO go into picture view
////            Toast.makeText(getActivity, pictureList[position].title, Toast.LENGTH_SHORT).show()
//        }
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPictureImg: ImageView = itemView.findViewById(R.id.ivPictureImg)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}