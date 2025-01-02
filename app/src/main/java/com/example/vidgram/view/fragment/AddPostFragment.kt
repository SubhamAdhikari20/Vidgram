package com.example.vidgram.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityBottomNavigationBinding
import com.example.vidgram.databinding.FragmentAddPostBinding
import com.example.vidgram.databinding.FragmentHomeBinding

class AddPostFragment : DialogFragment() {
    private lateinit var binding : FragmentAddPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)

        // Set up click listeners for the options
        binding.takePhotoPostTexView.setOnClickListener {

            dismiss()
        }

        binding.uploadGalleryPhotoTexView.setOnClickListener {

            dismiss()
        }


        return binding.root
    }

    companion object {
    }
}