package com.example.vidgram.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vidgram.view.fragment.OthersProfileFragment
import com.example.vidgram.view.fragment.PhotoGridFragment
import com.example.vidgram.view.fragment.VideosMyProfileFragment

class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PhotoGridFragment() // Grid for photos
            1 -> VideosMyProfileFragment() // Grid for videos
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
