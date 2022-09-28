package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.ItemPostBinding
import com.example.instagram.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostAdapter(options: FirestoreRecyclerOptions<Post>) : FirestoreRecyclerAdapter<Post, PostCardViewHolder>(options) {
    private lateinit var binding: ItemPostBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCardViewHolder {
        binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostCardViewHolder, position: Int, model: Post) {
        holder.bindTo(getItem(position))
    }
}

class PostCardViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(post: Post) {
        with(binding){
            postTextView.text = post.text
            Glide.with(postImage.context)
                .load(post.imageURL)
                .into(postImage)
            GlobalScope.launch(Dispatchers.IO){
                val user = UserDao.UserSingleton.INSTANCE.getUser(post.createdBy)
                withContext(Dispatchers.Main){
                    Glide.with(userImage.context)
                        .load(user?.imageUrl)
                        .circleCrop()
                        .into(userImage)
                    userNameView.text = user?.userName
                }
            }
        }
    }

}
