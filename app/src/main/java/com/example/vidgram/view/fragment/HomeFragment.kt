package com.example.vidgram.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentHomeBinding
import com.example.vidgram.databinding.FragmentMyProfileBinding
import com.example.vidgram.view.adapter.ViewPagerAdapter


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.profileButton.setOnClickListener {
            replaceFragment(MyProfileFragment())
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager : FragmentManager = parentFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    companion object {

    }
}