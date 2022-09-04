package com.example.instagram.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.instagram.adapters.GridAdapter
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.models.User
import kotlinx.coroutines.*

private const val UID_PARAM = "uid_params"

class ProfileFragment : Fragment() {
    object ProfileSingleton {
        val INSTANCE = ProfileFragment()
    }

    companion object {
        fun newInstance(uid: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(UID_PARAM, uid)
                }
            }
    }

    private var userID: String? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userDao: UserDao
    private lateinit var adapter: GridAdapter
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getString(UID_PARAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            userDao = UserDao.UserSingleton.INSTANCE
        if(userID==null)
            user = userDao.currentUser
        else
            user = userDao.SearchedUser
        setupView()
    }

    private fun setupView() {
        with(binding){
            Glide.with(imageView.context)
                .load(user.imageUrl)
                .circleCrop()
                .into(imageView)
            followersCountView.text = user.followers.size.toString()
            followingsCountView.text = user.followings.size.toString()
            userNameView.text = user.userName

            val options = userDao.getOptions(user.uid, "posts")
            adapter = GridAdapter(options)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = GridLayoutManager(activity, 3)
        }

        // TO REMOVE
        binding.fabBtn.setOnClickListener{
            userDao.addPost("hello", "https://img.freepik.com/free-vector/cute-ninja-with-sword-cartoon-flat-cartoon-style_138676-2762.jpg?w=2000")
        }
    }

    override fun onStart() {
        super.onStart()

        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }



}