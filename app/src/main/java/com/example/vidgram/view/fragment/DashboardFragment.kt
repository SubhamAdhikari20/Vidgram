package com.example.vidgram.view.fragment

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

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewStories)

        // Set up horizontal layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Sample data for stories
        val stories = listOf(
            Story("John", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic),
            Story("Alice", R.drawable.profile_pic)
        )

        // Set adapter
        recyclerView.adapter = StoryAdapter(stories)
    }
}
