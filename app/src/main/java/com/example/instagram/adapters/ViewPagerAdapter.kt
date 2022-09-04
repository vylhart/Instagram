package com.example.instagram.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.instagram.fragments.HomeFragment
import com.example.instagram.fragments.ProfileFragment
import com.example.instagram.fragments.SearchFragment

class ViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SearchFragment.SearchSingleton.INSTANCE
            1 -> HomeFragment.HomeSingleton.INSTANCE
            else -> ProfileFragment.ProfileSingleton.INSTANCE
        }
    }

}
