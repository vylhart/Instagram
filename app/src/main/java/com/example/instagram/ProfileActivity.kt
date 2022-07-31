package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
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
        setNavigation()
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