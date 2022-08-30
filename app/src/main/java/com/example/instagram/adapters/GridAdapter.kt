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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class GridAdapter(options: FirestoreRecyclerOptions<Post>) : FirestoreRecyclerAdapter<Post, PostViewHolder>(
    options
) {
    private lateinit var binding: ItemGridPostBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        binding = ItemGridPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
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
/*
private val DIFF_CALLBACK: DiffUtil.ItemCallback<Post> = object : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldData: Post, newData: Post): Boolean {
        return oldData.id == newData.id
    }

    override fun areContentsTheSame(oldData: Post, newData: Post): Boolean {
        return oldData == newData
    }
}
*/
