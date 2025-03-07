package com.example.vidgram.view.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityStoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)
        binding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.three_dot_icon2)
        val username = intent.getStringExtra("username")
        val storyImage = intent.getStringExtra("story_image")

        val timestamp = intent.getLongExtra("timestamp", -1L) // Get the timestamp in milliseconds

        if (timestamp != -1L) {
            // Create a Date object from the timestamp
            val date = Date(timestamp)

            // Format the date to include AM/PM
            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
            val formattedDate = dateFormat.format(date) // Convert the Date to a string with AM/PM

            // Use the formatted date (for example, displaying it in a TextView)
            binding.timeStampStoryTextView.text = formattedDate
        }



        binding.nameStoryTexView.text = username // Display the username
        if (storyImage != null) {
            Glide.with(this) // Load story image with Glide
                .load(storyImage) // Pass the image URL or resource
                .into(binding.storyImageView)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.storyLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Go back to the previous fragment or activity
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    // Inflate the toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.story_menu_toolbar, menu)
        return true
    }
}