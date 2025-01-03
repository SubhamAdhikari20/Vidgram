package com.example.vidgram.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentMyProfileBinding
import com.example.vidgram.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    var icons = arrayOf(
        R.drawable.photo_icon,
        R.drawable.video_icon,
    )

    var data = arrayOf(
        "Active Order",
        "Cancel Order",
        "Delivered Order",
    )

    override fun onPause() {
        Log.d("lifecycle", "onPause -> I am called")
        super.onPause()
    }

    override fun onResume() {
        Log.d("lifecycle", "onResume -> I am called")
        super.onResume()
    }

    override fun onStart() {
        Log.d("lifecycle", "onStart -> I am called")
        super.onStart()
    }

    override fun onDestroy() {
        Log.d("lifecycle", "onDestroy -> I am called")
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "OnCreate -> I am called")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        val fragmentManager: FragmentManager = childFragmentManager
        viewPagerAdapter = ViewPagerAdapter(fragmentManager, lifecycle)
        binding.myProfileViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.myProfileTableLayout, binding.myProfileViewPager) {
//            tabs, position -> tabs.text = data[position]
                tabs, position ->
            tabs.icon = resources.getDrawable(icons[position], null)
        }.attach()

        binding.editProfileButton.setOnClickListener {
            replaceFragment(OthersProfileFragment())
        }

        /*
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragment(PhotosMyProfileFragment(), "Photo")
        viewPagerAdapter.addFragment(VideosMyProfileFragment(), "Video")
        binding.myProfileViewPager.adapter = viewPagerAdapter
        binding.myProfileTableLayout.setupWithViewPager(binding.myProfileViewPager)

        // Set custom icons for each tab
        binding.myProfileTableLayout.getTabAt(0)?.setIcon(R.drawable.photo_icon)
        binding.myProfileTableLayout.getTabAt(1)?.setIcon(R.drawable.video_icon)
        */
        return binding.root
    }

    companion object {

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}