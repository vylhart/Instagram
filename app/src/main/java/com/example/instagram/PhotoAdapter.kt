package com.example.instagram

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlin.math.log

class PhotoAdapter(var context: Context, private var dataList: List<ItemModel>): RecyclerView.Adapter<PhotoAdapter.ViewHolder>(){
    private val TAG = Utils.TAG+ "PhotoAdapter"
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        init {
            image = itemView.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(dataList[position].image)
            .centerCrop()
            .fit()
            .placeholder(R.drawable.user)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}