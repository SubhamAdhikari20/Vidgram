package com.example.vidgram.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentHomeBinding
import com.example.vidgram.view.adapter.StoryAdapter
import com.example.vidgram.view.adapter.PostAdapter
import com.example.vidgram.view.model.Post
import com.example.vidgram.view.model.Story

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Navigate to profile fragment on button click
        binding.profileButton.setOnClickListener {
            replaceFragment(MyProfileFragment())
        }

        // Add Story Button Logic
        binding.addStoryImage.setOnClickListener {
            openAddStoryFragment()
        }

        // Initialize RecyclerView for stories
        binding.recyclerViewStories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val stories = listOf(
            Story("John", R.drawable.person1),
            Story("Alice", R.drawable.person1),
            Story("Bob", R.drawable.person1),
            Story("Emma", R.drawable.person1)
        )
        val storyAdapter = StoryAdapter(stories)
        binding.recyclerViewStories.adapter = storyAdapter

        // Initialize RecyclerView for posts
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())

        val posts = listOf(
            Post("John Doe", R.drawable.my_story_icon, R.drawable.person1, "Enjoying the sunset!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Alice Smith", R.drawable.my_story_icon, R.drawable.person1, "Had a great day!", "12:00", "24k", "1k", "1,080", "2.4k")
        )
        val postAdapter = PostAdapter(posts)
        binding.recyclerViewPosts.adapter = postAdapter

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun openAddStoryFragment() {
        replaceFragment(AddStoryFragment())
    }
}
