package com.example.instagram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instagram.R
import com.example.instagram.adapters.ViewPagerAdapter
import com.example.instagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
    }
    private fun setNavigation() {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    binding.viewpager.currentItem = 0
                    true
                }
                R.id.search -> {
                    binding.viewpager.currentItem = 1
                    true
                }
                else -> {
                    binding.viewpager.currentItem = 2
                    true
                }
            }
        }
    }
}