package com.example.vidgram.view.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentAddPostBinding
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.model.Post
import com.example.vidgram.view.activity.NewPostActivity
import com.example.vidgram.viewmodel.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddPostFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentAddPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listeners for the options
        binding.takePhotoLinearLayout.setOnClickListener {
            val intent = Intent(requireContext(), NewPostActivity::class.java)
            startActivity(intent)
        }

        binding.uploadGalleryLinearLayout.setOnClickListener {
            val intent = Intent(requireContext(), NewPostActivity::class.java)
            startActivity(intent)
        }

    }
    private fun openNewPostActivity() {
        val intent = Intent(requireContext(), NewPostActivity::class.java)
    }


}