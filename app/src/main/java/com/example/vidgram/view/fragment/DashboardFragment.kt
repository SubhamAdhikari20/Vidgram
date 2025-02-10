package com.example.vidgram.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.R
import com.example.vidgram.model.Story
import com.example.vidgram.adapter.StoryAdapter
import com.example.vidgram.databinding.FragmentDashboardBinding
import com.example.vidgram.model.StoryModel
import com.example.vidgram.view.activity.StoryActivity

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var storylist: MutableList<StoryModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewStories)

        // Set up horizontal layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Sample data for stories
//        val stories = listOf(
//            Story("John", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic),
//            Story("Alice", R.drawable.profile_pic)
//        )

        // Set adapter
        storylist = mutableListOf()

        recyclerView.adapter= StoryAdapter(storylist) { story ->
            // Handle the click event, navigate to the next activity
            val intent = Intent(requireContext(), StoryActivity::class.java)
            intent.putExtra("story_id", story.storyId) // Pass story ID (or other details)
            startActivity(intent)
        }
    }


}
