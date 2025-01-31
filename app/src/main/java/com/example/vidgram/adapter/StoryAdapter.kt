package com.example.vidgram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.model.Story
import com.example.vidgram.model.StoryModel

class StoryAdapter(private val stories: List<StoryModel>,
                   private val onItemClick: (StoryModel) -> Unit // Add a callback for item clicks
) :

    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]

        // Check if storyIma is not null or empty and load the image
        val storyImageUrl = story.storyImage ?: ""
        if (storyImageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context) // Use context from itemView
                .load(storyImageUrl)             // Use storyIma to load the image
                .into(holder.storyImage)         // Load into storyImage ImageView
        } else {
            holder.storyImage.setImageResource(R.drawable.person1) // Set a placeholder if image URL is empty
        }

        // Set the username for the story
        holder.userName.text = story.username

        holder.itemView.setOnClickListener {
            onItemClick(story) // Trigger the callback with the clicked story
        }
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyImage: ImageView = itemView.findViewById(R.id.storyImageView) // ImageView for story image
        val userName: TextView = itemView.findViewById(R.id.storyUsername)      // TextView for username


    }
}
