package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.databinding.ItemGridPostBinding
import com.example.instagram.models.Post

class GridAdapter() : ListAdapter<Post, PostViewHolder>(DIFF_CALLBACK) {
    private lateinit var binding: ItemGridPostBinding
    private val list: ArrayList<Post> = ArrayList()

    fun addItem(post: Post){
        list.add(post)
        submitList(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemGridPostBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

class PostViewHolder(private val binding: ItemGridPostBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(post: Post) {
        Glide.with(binding.itemImageVIew.context)
            .load(post.imageURL)
            .centerCrop()
            .placeholder(R.drawable.user)
            .into(binding.itemImageVIew)
    }
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Post> = object : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldData: Post, newData: Post): Boolean {
        return oldData.id == newData.id
    }

    override fun areContentsTheSame(oldData: Post, newData: Post): Boolean {
        return oldData == newData
    }
}