package com.example.vidgram.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityNewPostBinding
import com.example.vidgram.model.PostModel
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.PostViewModel
import com.example.vidgram.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseUser

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel
    private lateinit var loadingDialogUtils: LoadingDialogUtils

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)

        // Initialize ViewModels
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)


        // Post Backend Binding
        val postRepo = PostRepositoryImpl()
        postViewModel = PostViewModel(postRepo)

        // Initialize Cloudinary
        initializeCloudinary()

        // Fetch current user data
        fetchCurrentUser()

        setSupportActionBar(binding.newPostToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        // Setup text watcher for post description
        setupDescriptionTextWatcher()

        // Initialize image picker and camera launchers
        initializeImagePickers()

        // Handle image selection
        binding.addPostImageView.setOnClickListener {
            checkPermissionsAndOpenPicker()
        }

        // Handle post submission
        binding.postButton.setOnClickListener {
            submitPost()
        }

        fetchUserData()

        // Handle window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.newPostLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializeCloudinary() {
        try {
            val config = mutableMapOf<String, String>()
            config["cloud_name"] = "dbukovsi1"
            config["api_key"] = "718742783263144"
            config["api_secret"] = "udtElGelBPWkalRKw-RQrnqFRI8"

            MediaManager.init(this, config)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun fetchCurrentUser() {
        val currentUser = userViewModel.getCurrentUser()
        currentUser?.let {
            userViewModel.getUserFromDatabase(it.uid.toString())
        }

        userViewModel.userData.observe(this) { user ->
            binding.newPostUserName.text = user?.fullName.toString()
        }
    }

    private fun setupDescriptionTextWatcher() {
        binding.postDescEditTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePostButtonState(s?.isNotEmpty() == true)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun initializeImagePickers() {
        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.postImageView.setImageURI(it)
                selectedImageUri = uri
                binding.postImageView.visibility = View.VISIBLE
                updatePostButtonState(true)
            }
        }

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    binding.postImageView.setImageBitmap(it)
                    binding.postImageView.visibility = View.VISIBLE
                    updatePostButtonState(true)
                }
            }
        }

        // Initialize the permissions launcher
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                openImagePickerDialog()
            } else {
                Toast.makeText(this, "Permissions required to proceed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePostButtonState(isEnabled: Boolean) {
        if (isEnabled) {
            binding.postButton.background = ContextCompat.getDrawable(this, R.drawable.post_filled_custom_btn)
            binding.postButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.postButton.background = ContextCompat.getDrawable(this, R.drawable.post_custom_btn)
            binding.postButton.setTextColor(ContextCompat.getColor(this, R.color.postBtnTextColor))
        }
    }

    // Function to check and request permissions
    private fun checkPermissionsAndOpenPicker() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        }
        else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        if (requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            openImagePickerDialog()
        }
        else {
            permissionsLauncher.launch(requiredPermissions)
        }
    }

    private fun openImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Take a Photo")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> { // Open Gallery
                    imagePickerLauncher.launch("image/*")
                }
                1 -> { // Open Camera
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(packageManager) != null) {
                        cameraLauncher.launch(takePictureIntent)
                    } else {
                        Toast.makeText(this, "Camera not available!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        builder.show()
    }

    private fun submitPost() {
        loadingDialogUtils.show()

        val postDesc: String = binding.postDescEditTextField.text.toString()
        val postImage: String = selectedImageUri.toString()

        // Observe the userData LiveData
        userViewModel.userData.observe(this) { currentUser ->
            currentUser?.let {
                val profileImage: String? = it.profilePicture?.toString() // Use profile picture URL from user data
                val postTimeStamp: Long = System.currentTimeMillis()

                val postModel = PostModel(
                    postId = "",
                    postImage = postImage,
                    profileImage = profileImage,
                    postDescription = postDesc,
                    username = binding.newPostUserName.text.toString(),
                    postTimeStamp = postTimeStamp.toString()
                )

                // Call ViewModel method to add the post
                postViewModel.addPost(postModel) { success, message ->
                    if (success) {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                    loadingDialogUtils.dismiss()
                }
            } ?: run {
                Toast.makeText(this, "User data is unavailable", Toast.LENGTH_SHORT).show()
                loadingDialogUtils.dismiss()
            }
        }
    }

    private fun fetchUserData() {
        val currentUser = userViewModel.getCurrentUser()
        currentUser?.let {
            userViewModel.getUserFromDatabase(it.uid) // Fetch user data using the current user ID
        } ?: run {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
