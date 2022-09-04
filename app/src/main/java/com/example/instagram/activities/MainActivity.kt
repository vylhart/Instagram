package com.example.instagram.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.instagram.R
import com.example.instagram.adapters.ViewPagerAdapter
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.fragments.ProfileFragment
import com.example.instagram.models.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
    }

    fun onUserClicked(user: User) {
        UserDao.UserSingleton.INSTANCE.SearchedUser = user
        startActivity(Intent(this, UserActivity::class.java))
    }

    private fun setNavigation() {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.search -> {
                    binding.viewpager.currentItem = 0
                    true
                }
                R.id.home -> {
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