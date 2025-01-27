package com.example.vidgram.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        binding.nameProfileCardView.setOnClickListener {
            val intent = Intent(this@EditProfileActivity, BioEditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.bioProfileCardView.setOnClickListener {
            val intent = Intent(this@EditProfileActivity, BioEditProfileActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editProfileLayout)) { v, insets ->
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
}