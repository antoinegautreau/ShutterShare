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
import ca.unb.mobiledev.shuttershare.entity.Folder

class MyFolderAdapter constructor(
    private val getActivity: Activity,
    private val folderList: List<Folder>) :
    RecyclerView.Adapter<MyFolderAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvFolderTitle.text = folderList[position].title
        holder.ivFolderImg.setImageResource(folderList[position].image)

        holder.cardView.setOnClickListener {
            val intent = Intent(getActivity, PicturesActivity::class.java)
            intent.putExtra("EventName", folderList[position].title)
            getActivity.startActivity(intent)

            Toast.makeText(getActivity, folderList[position].title, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFolderTitle: TextView = itemView.findViewById(R.id.tvFolderTitle)
        val ivFolderImg: ImageView = itemView.findViewById(R.id.ivFolderImg)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

}