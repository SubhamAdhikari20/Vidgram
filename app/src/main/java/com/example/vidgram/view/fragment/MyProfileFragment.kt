package com.example.vidgram.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentMyProfileBinding
import com.example.vidgram.view.adapter.ViewPagerAdapter

class MyProfileFragment : Fragment() {
    private lateinit var binding : FragmentMyProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragment(PhotosMyProfileFragment(), "Photo")
        viewPagerAdapter.addFragment(VideosMyProfileFragment(), "Video")
        binding.myProfileViewPager.adapter = viewPagerAdapter
        binding.myProfileTableLayout.setupWithViewPager(binding.myProfileViewPager)

        // Set custom icons for each tab
        binding.myProfileTableLayout.getTabAt(0)?.setIcon(R.drawable.photo_icon)
        binding.myProfileTableLayout.getTabAt(1)?.setIcon(R.drawable.video_icon)

        return binding.root
    }

    companion object {

    }

//    override fun onStart() {
//        super.onStart()
//
//    }
}