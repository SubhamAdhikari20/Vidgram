package com.example.vidgram.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentAddPostBinding
import com.example.vidgram.databinding.FragmentNewAddPostBinding
import com.example.vidgram.model.Post
import com.example.vidgram.viewmodel.SharedViewModel

class NewAddPost : Fragment() {
    private lateinit var binding: FragmentNewAddPostBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAddPostBinding.inflate(inflater, container, false)

        binding.submitPostButton.setOnClickListener {
            val postContent = binding.postContentEditText.text.toString()
            val post = Post(
                "User", R.drawable.my_story_icon, R.drawable.person1,
                postContent, "Just Now", "0", "0", "0", "0"
            )
            sharedViewModel.addPost(post)  // Pass the new post to the ViewModel
            parentFragmentManager.popBackStack()  // Go back to HomeFragment
        }

        return binding.root
    }
}
