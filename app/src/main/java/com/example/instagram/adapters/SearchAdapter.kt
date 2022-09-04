package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.databinding.ItemSearchBinding
import com.example.instagram.models.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchAdapter(options: FirestoreRecyclerOptions<User>, private val onUserClicked: (User)->Unit) : FirestoreRecyclerAdapter<User, UserViewHolder>(
    options
) {
    private lateinit var binding: ItemSearchBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemSearchBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, onUserClicked)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.bindTo(getItem(position))
    }
}

class UserViewHolder(private val binding: ItemSearchBinding, private val onUserClicked: (User) -> Unit, ) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(user: User) {
        with(binding){
            Glide.with(binding.imageView.context)
                .load(user.imageUrl)
                .circleCrop()
                .into(binding.imageView)
            userNameView.text  = user.userName
            card.setOnClickListener {
                onUserClicked(user)
            }
        }
    }


}
