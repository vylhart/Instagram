package com.example.instagram

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.databinding.FragmentSearchBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class SearchFragment : Fragment() {
    private val TAG = "SearchFragment"
    private lateinit var filePath: Uri
    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.uploadBtn.setOnClickListener { uploadImage() }
        binding.selectBtn.setOnClickListener { selectImage() }
    }

    private fun selectImage() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 101)
    }

    private fun uploadImage() {
        if(filePath != null){
            var pg = ProgressDialog(activity)
            pg.setTitle("Uploading....")
            pg.show()
            val storageRef = FirebaseStorage.getInstance().reference.child("posts/${UUID.randomUUID()}")
            var task = storageRef.putFile(filePath)
            task.addOnFailureListener {
                Log.d(TAG, "onViewCreated: failed")
            }.addOnSuccessListener {
                Log.d(TAG, "onViewCreated: passed")
                pg.dismiss()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101 && resultCode==RESULT_OK && data!=null && data.data!=null){
            filePath = data.data!!
            try{
                var bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,filePath)
                binding.imageView.setImageBitmap(bitmap)
            }
            catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

}