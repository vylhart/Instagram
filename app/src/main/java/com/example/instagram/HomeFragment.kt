package com.example.instagram

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagram.databinding.FragmentHomeBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class HomeFragment : Fragment() {

    private lateinit var photoAdapter: PhotoAdapter
    private var dataList = mutableListOf<ItemModel>()
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList()
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, 3)
            photoAdapter = PhotoAdapter(activity, dataList)
            adapter = photoAdapter
        }
    }

    private fun createList() {


        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
        dataList.add(ItemModel("Title", "Desc", R.drawable.user))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

}