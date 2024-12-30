package com.example.vidgram.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentHomeBinding
import com.example.vidgram.databinding.FragmentMyProfileBinding
import com.example.vidgram.view.adapter.PostFeedRecyclerViewAdapter
import com.example.vidgram.view.adapter.ViewPagerAdapter
import com.example.vidgram.view.model.Post
import com.example.vidgram.view.model.Story
import com.example.vidgram.view.model.StoryAdapter
import com.example.week2.adapter.PostAdapter


class HomeFragment<StoryRecyclerViewAdapter> : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    // Story Recycler View
    private val storyImageList: ArrayList<Int> = ArrayList()
    private val storyNameList: ArrayList<String> = ArrayList()
//    private lateinit var storyAdapter : StoryRecyclerViewAdapter


    // Post Feed Recycler View
    private val postImageList: ArrayList<Int> = ArrayList()
    private val postProfileImageList: ArrayList<Int> = ArrayList()
    private val postNameList: ArrayList<String> = ArrayList()
    private val messageList: ArrayList<String> = ArrayList()
    private lateinit var postFeedAdapter : PostFeedRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        // Initialize RecyclerView for stories
        binding.recyclerViewStories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val stories = listOf(
            Story("John", R.drawable.person1),
            Story("Alice", R.drawable.person1),
            Story("Bob", R.drawable.person1),
            Story("Emma", R.drawable.person1),
            Story("John", R.drawable.person1),
            Story("Alice", R.drawable.person1),
            Story("Bob", R.drawable.person1),
            Story("Emma", R.drawable.person1),
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
            Post("John Doe", R.drawable.person1, R.drawable.person1, "Enjoying the sunset!"),
            Post("Alice Smith", R.drawable.person1, R.drawable.person1, "Had a great day!"),
            Post("Bob Lee", R.drawable.person1, R.drawable.person1, "Coffee break!"),
            Post("Emma Brown", R.drawable.person1, R.drawable.person1, "Amazing hike!"),
            Post("John Doe", R.drawable.person1, R.drawable.person1, "Enjoying the sunset!"),
            Post("Alice Smith", R.drawable.person1, R.drawable.person1, "Had a great day!"),
            Post("Bob Lee", R.drawable.person1, R.drawable.person1, "Coffee break!"),
            Post("Emma Brown", R.drawable.person1, R.drawable.person1, "Amazing hike!")
        )

        val postAdapter = PostAdapter(posts)
        binding.recyclerViewPosts.adapter = postAdapter

        // Add scroll listener for fade-in/out effect


        return binding.root
    }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}
