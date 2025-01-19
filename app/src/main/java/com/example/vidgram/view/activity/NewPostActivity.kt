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

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var userViewModel: UserViewModel
    lateinit var postViewModel: PostViewModel
    lateinit var loadingDialogUtils: LoadingDialogUtils


    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)

        val config = mutableMapOf<String, String>()
        config["cloud_name"] = "drykew7pu"
        config["api_key"] = "891342176588327"
        config["api_secret"] = "-7N8kuVvR0FNLLPYFModBB_03UM"

        MediaManager.init(this, config)

        // User Backend Binding
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)


        // Post Backend Binding
        val postRepo = PostRepositoryImpl()
        postViewModel = PostViewModel(postRepo)


        val currentUser = userViewModel.getCurrentUser()
        currentUser.let{    // it -> currentUser
            userViewModel.getUserFromDatabase(it?.uid.toString())
        }
        
        userViewModel.userData.observe(this){
            binding.newPostUserName.text = it?.fullName.toString()

        }

        setSupportActionBar(binding.newPostToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        binding.postDescEditTextField.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updatePostButtonState(s?.isNotEmpty() == true)
                /*
                var description : String = binding.postDescEditTextField.text.toString()
                if (description.isNotEmpty()) {
                    binding.postButton.background = ContextCompat.getDrawable(this@NewPostActivity, R.drawable.post_filled_custom_btn)
                    binding.postButton.setTextColor(ContextCompat.getColor(this@NewPostActivity, R.color.white))

                }
                else {
                    // Reset to a default or inactive color
                    binding.postButton.background = ContextCompat.getDrawable(this@NewPostActivity, R.drawable.post_custom_btn)
                    binding.postButton.setTextColor(ContextCompat.getColor(this@NewPostActivity, R.color.postBtnTextColor))
                }
                 */
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.postImageView.setImageURI(it)
                selectedImageUri = uri


                binding.postImageView.visibility = View.VISIBLE
                updatePostButtonState(true)
            }
        }

        // Initialize the camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    binding.postImageView.setImageBitmap(it)
                    binding.postImageView.visibility = android.view.View.VISIBLE
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

        // Handle "Add Image" button click
        binding.addPostImageView.setOnClickListener {
            checkPermissionsAndOpenPicker()
        }


        // Post Button
        binding.postButton.setOnClickListener {
            loadingDialogUtils.show()
            var postDesc : String? = binding.postDescEditTextField.text.toString()
            var postImage : String? = selectedImageUri.toString()
            var postBy : String? = binding.newPostUserName.text .toString()
            var postTimeStamp : String? = binding.newPostUserName.text .toString()
            val profileImage: String? = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHtVmYuLaQpNM-dToMMFc3BSl0L-dXfhJX3A&s"
            var postModel = PostModel("", postImage, profileImage,postDesc, postBy, postTimeStamp)

            postViewModel.addPost(postModel,this@NewPostActivity){
                success, message ->
                if (success){
                    Toast.makeText(this@NewPostActivity, message, Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    Toast.makeText(this@NewPostActivity, message, Toast.LENGTH_LONG).show()
                }
                loadingDialogUtils.dismiss()
            }
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.newPostLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.post_menu_toolbar, menu)
//
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
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


}