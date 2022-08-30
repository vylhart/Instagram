package com.example.instagram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.instagram.R
import com.example.instagram.fragments.ProfileFragment

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val fragment = ProfileFragment.newInstance("todo()")
        val fragmentManager: FragmentManager = supportFragmentManager
        var ft = fragmentManager.beginTransaction()
        ft.replace(R.id.frameLayout, fragment)
        ft.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}