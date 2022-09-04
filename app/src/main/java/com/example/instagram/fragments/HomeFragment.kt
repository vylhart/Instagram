package com.example.instagram.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.adapters.PostAdapter
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.databinding.ItemPostBinding
import com.example.instagram.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query


class HomeFragment : Fragment() {
    object HomeSingleton {
        val INSTANCE = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PostAdapter
    private lateinit var userDao: UserDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDao = UserDao.UserSingleton.INSTANCE
        val options = userDao.getOptions(userDao.currentUser.uid,"timeline")
        adapter = PostAdapter(options)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
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