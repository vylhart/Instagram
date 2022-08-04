package com.example.instagram

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.instagram.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

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
            layoutManager = GridLayoutManager(activity,3)
            photoAdapter = PhotoAdapter(context,dataList)
            adapter = photoAdapter
        }
        getUserDetails()
    }

    private fun getUserDetails(){
        var auth = Firebase.auth
        var user = auth.currentUser?.uid
        var task = FirebaseStorage.getInstance().reference.child("posts").child(user.toString()).downloadUrl
        task.addOnSuccessListener {
            activity?.let { ctx -> Glide.with(ctx).load(it).optionalCircleCrop().into(binding.imageView) }
            //Picasso.get().load(it).into(binding.imageView)
            Log.d(TAG, "getUserDetails: passed")
        }.addOnFailureListener {
            Log.d(TAG, "getUserDetails: failed")
            it.printStackTrace()
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