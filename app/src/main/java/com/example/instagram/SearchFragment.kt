package com.example.instagram

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.*
import kotlin.math.log

class SearchFragment : Fragment() {
    private val TAG = Utils.TAG + "SearchFragment"
    private lateinit var filePath: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var user: FirebaseUser


    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        binding.userId.text = user.uid
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
        var pg = ProgressDialog(activity)
        pg.setTitle("Uploading....")
        pg.show()
        var uid = UUID.randomUUID()
        val storageRef = FirebaseStorage.getInstance().reference.child("posts/${user.uid}")
        val dbRef  = Firebase.database.reference.child("users").child(user.uid).child("photo")
        dbRef.setValue(uid.toString()).addOnFailureListener {
            Log.d(TAG, "uploadImage: failed")
            it.printStackTrace()
        }.addOnSuccessListener {
            Log.d(TAG, "uploadImage: value pushed")
        }
        var task = storageRef.putFile(filePath)
        task.addOnFailureListener {
            Log.d(TAG, "onViewCreated: failed")
        }.addOnSuccessListener {

            Log.d(TAG, "onViewCreated: passed")
            pg.dismiss()
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