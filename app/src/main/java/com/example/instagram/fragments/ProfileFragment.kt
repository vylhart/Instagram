package com.example.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.Utils.Companion.TAG
import com.example.instagram.adapters.GridAdapter
import com.example.instagram.daos.PostDao
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {
    object ProfileSingleton {
        val INSTANCE = ProfileFragment()
    }

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userDao: UserDao
    private lateinit var postDao: PostDao
    private lateinit var adapter: GridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDao = UserDao.UserSingleton.INSTANCE
        postDao = PostDao.PostSingleton.INSTANCE
        setupRecyclerView()
        setupView()

        binding.fabBtn.setOnClickListener{
            postDao.addPost("hello", "https://img.freepik.com/free-vector/cute-ninja-with-sword-cartoon-flat-cartoon-style_138676-2762.jpg?w=2000")
        }
    }

    private fun setupView() {
        with(binding){
            val user = userDao.currentUser
            Glide.with(imageView.context)
                .load(user.imageUrl)
                .circleCrop()
                .into(imageView)
            followersCountView.text = user.followers.size.toString()
            followingsCountView.text = user.followings.size.toString()
            postsCountView.text = user.posts.size.toString()
            userNameView.text = user.userName
        }
    }


    private fun setupRecyclerView() {
        adapter = GridAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(activity, 3)
        val postList = userDao.currentUser.posts
        for(postId in postList){
            GlobalScope.launch(Dispatchers.IO){
                val post = postDao.getPost(postId)
                withContext(Dispatchers.Main){
                    Log.d(TAG, "setupRecyclerView: ")
                    post?.let { adapter.addItem(it) }
                }
            }
        }
    }

}