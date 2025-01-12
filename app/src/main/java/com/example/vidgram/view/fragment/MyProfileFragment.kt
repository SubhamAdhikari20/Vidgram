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
import com.example.vidgram.repository.UserAuthRepositoryImpl
import com.example.vidgram.viewmodel.UserAuthViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var userAuthViewModel: UserAuthViewModel

    private var icons = arrayOf(
        R.drawable.photo_icon,
        R.drawable.video_icon,
    )

    var data = arrayOf(
        "Active Order",
        "Cancel Order",
        "Delivered Order",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserAuthRepositoryImpl()
        userAuthViewModel = UserAuthViewModel(repo)

        val currentUser = userAuthViewModel.getCurrentUser()
        currentUser.let{    // it -> currentUser
            Log.d("userId",it?.uid.toString())
            userAuthViewModel.getUserFromDatabase(it?.uid.toString())
        }


        userAuthViewModel.userData.observe(requireActivity()){
            binding.nameTextView.text = it?.fullName.toString()

        }

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

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}