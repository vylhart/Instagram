package com.example.instagram

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.databinding.ActivityProfileBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class ProfileActivity : AppCompatActivity() {
    private val TAG = Utils.TAG + "ProfileActivity"
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
        var user = auth.currentUser
        if(user==null){
            signInAnonymously()
            Log.d(TAG, "onStart: anonymous login")
        }
    }
    private fun signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener{
            Log.d(TAG, "signInAnonymously: success")
        }.addOnFailureListener{ exception ->
            Log.e(TAG, "signInAnonymously:FAILURE", exception)
        }
    }


    private fun setNavigation() {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.feed -> {
                    binding.viewpager.setCurrentItem(0)
                    true
                }
                R.id.search -> {
                    binding.viewpager.setCurrentItem(1)
                    true
                }
                else -> {
                    binding.viewpager.setCurrentItem(2)
                    true
                }
            }
        }
    }


}