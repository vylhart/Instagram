package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView

class ProfileActivity : AppCompatActivity() {
    private val TAG = "ProfileActivity"
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.feed -> {
                    Toast.makeText(this, "Feed selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.search -> {
                    Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }



    }



}