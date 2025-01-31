package com.example.vidgram.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityAddStoryBinding
import com.example.vidgram.databinding.ActivityStoryBinding
import com.example.vidgram.model.StoryModel
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.repository.StoryRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.ImageUtils
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.PostViewModel
import com.example.vidgram.viewmodel.StoryViewModel
import com.example.vidgram.viewmodel.UserViewModel

class AddStoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddStoryBinding

    private lateinit var imageUtils: ImageUtils
    private lateinit var userViewModel: UserViewModel
    lateinit var storyViewModel: StoryViewModel
    lateinit var loadingDialogUtils: LoadingDialogUtils

    private var selectedImageUri: Uri? = null
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageUtils = ImageUtils(this)

        loadingDialogUtils = LoadingDialogUtils(this)
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)

        val repo = StoryRepositoryImpl()
        storyViewModel = StoryViewModel(repo)

        try {
            val config = mutableMapOf<String, String>()
            config["cloud_name"] = "dbukovsi1"
            config["api_key"] = "718742783263144"
            config["api_secret"] = "udtElGelBPWkalRKw-RQrnqFRI8"

            MediaManager.init(this, config)

        } catch (_: Exception) {
            // Ignore errors silently
        }

        val currentUser = "tRYDhugrv3anlPSlN4n0Dovtt602"
        userViewModel.getUserFromDatabase(currentUser)

        userViewModel.userData.observe(this) { user ->
            userName = user?.fullName.toString()
            binding.userName.text = userName

            // Enable the upload button once the username is available
            binding.uploadButton.isEnabled = userName.isNotEmpty()
        }

        imageUtils.registerActivity { uri ->
            selectedImageUri = uri
            if (uri != null) {
                binding.mediaPreview.setImageURI(uri)  // Show image in ImageView
                binding.mediaPreview.visibility = View.VISIBLE
            } else {
                Log.e("AddStoryActivity", "Image URI is null")
            }
        }

        // Choose Image Button
        binding.mediaPreview.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        binding.uploadButton.setOnClickListener {
            loadingDialogUtils.show()
            selectedImageUri?.let { uri ->
                var storyImage = uri.toString()
                val timestamp = System.currentTimeMillis()
                val storyModel = StoryModel("", storyImage, userName, timestamp)
                storyViewModel.addStory(storyModel) { success, message ->
                    if (success) {
                        Toast.makeText(this@AddStoryActivity, message, Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddStoryActivity, message, Toast.LENGTH_LONG).show()
                    }
                    loadingDialogUtils.dismiss()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
