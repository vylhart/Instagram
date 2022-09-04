package com.example.instagram.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.activities.MainActivity
import com.example.instagram.adapters.SearchAdapter
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    object SearchSingleton {
        val INSTANCE = SearchFragment()
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.toolbar.menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = UserDao.UserSingleton.INSTANCE.getUserOptions(newText)
                adapter = SearchAdapter(query) { userID -> (activity as MainActivity).onUserClicked(userID) }
                binding.recyclerview.adapter = adapter
                adapter.startListening()
                return true
            }
        })


        val query = UserDao.UserSingleton.INSTANCE.getUserOptions("")
        adapter = SearchAdapter(query) { user -> (activity as MainActivity).onUserClicked(user) }
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