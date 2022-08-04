package com.example.instagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class PhotoAdapter(var context: Context): RecyclerView.Adapter<PhotoAdapter.ViewHolder>(){
    private lateinit var dataList: List<ItemModel>

    fun setData(dataList: List<ItemModel >){
        this.dataList = dataList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        init {
            image = itemView.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(dataList[position].image)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}