package com.example.instagram

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagram.databinding.FragmentHomeBinding
import com.google.firebase.storage.FirebaseStorage

class HomeFragment : Fragment() {
    private val TAG = Utils.TAG + "HomeFragment"
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var binding: FragmentHomeBinding
    private var dataList = mutableListOf<ItemModel>()


    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList()
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity,2)
            photoAdapter = PhotoAdapter(context,dataList)
            adapter = photoAdapter
        }

    }

    private fun createList() {
        val storageRef = FirebaseStorage.getInstance().getReference("posts/")
        var pg = ProgressDialog(activity)
        pg.setTitle("Loading  images...")
        pg.show()

        var task = storageRef.listAll()
        task.addOnFailureListener {
            Log.d(TAG, "createList: failed")
            pg.dismiss()
        }.addOnSuccessListener {res->
            res.items.forEach{url->
                url.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "createList:${it}")
                    dataList.add(ItemModel("Title", "dss", it))
                    photoAdapter.notifyItemInserted(dataList.size-1)
                }
            }
            pg.dismiss()
        }
    }

}