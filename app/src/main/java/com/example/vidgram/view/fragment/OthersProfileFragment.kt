package com.example.vidgram.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.vidgram.R
import com.example.vidgram.adapter.ProfilePagerAdapter
import com.example.vidgram.databinding.FragmentOthersProfileBinding
import com.google.android.material.tabs.TabLayoutMediator

class OthersProfileFragment : Fragment() {
    private lateinit var binding : FragmentOthersProfileBinding
    var icons = arrayOf(
        R.drawable.photo_icon,
        R.drawable.video_icon,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false)

        val adapter = ProfilePagerAdapter(this)  // Pass the fragment as context
        binding.otherProfileViewPager.adapter = adapter

        // Set up TabLayout with icons

        TabLayoutMediator(binding.otherProfileTableLayout, binding.otherProfileViewPager) { tabs, position ->
            tabs.icon = resources.getDrawable(icons[position], null)
        }.attach()


        return binding.root
    }



    companion object {

    }
}