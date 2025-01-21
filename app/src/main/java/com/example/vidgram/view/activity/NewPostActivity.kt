package com.example.vidgram.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.Manifest
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityNewPostBinding
import com.example.vidgram.model.Post
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.viewmodel.PostViewModel
import com.example.vidgram.viewmodel.UserViewModel
import android.content.Context
import android.net.Uri


class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        // Initialize the PostViewModel to handle new post operations
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

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
                binding.postImageView.visibility = View.VISIBLE
                updatePostButtonState(true)

                // Save the URI as a String for later use
                val imageUriString = it.toString()
            }
        }

        // Initialize the camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    binding.postImageView.setImageBitmap(it)
                    binding.postImageView.visibility = View.VISIBLE
                    updatePostButtonState(true)

                    // Convert the Bitmap to URI and store the URI string
                    val imageUriString = getImageUri(this, it).toString()
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

//        binding.postButton.setOnClickListener {
//            try {
//                val postDescription = binding.postDescEditTextField.text.toString()
//
//                // Ensure the description is not empty
//                if (postDescription.isBlank()) {
//                    Toast.makeText(this, "Post description cannot be empty", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//
//                // Retrieve image drawable as a string
//                val imageUriString = binding.postImageView.drawable?.toString() ?: run {
//                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//
//                // User avatar URI
//                val userAvatarUriString = "android.resource://$packageName/${R.drawable.person1}"
//
////                // Create a new Post object
////                val post = Post(
////                    caption = postDescription,
////                    postImage = imageUriString,  // Store the URI string
////                    username = "Yogesh",
////                    userAvatar = userAvatarUriString, // Store the URI string for the avatar
////                    time = "2025",
////                    like = "0",
////                    dislike = "0",
////                    comment = "",
////                    share = ""
////                )
//
//                // Add the post to the ViewModel
////                postViewModel.addNewPost(post)
//
//                // Notify success
//                Toast.makeText(this, "Post added successfully!", Toast.LENGTH_SHORT).show()
//                val intent = Intent()
//                intent.putExtra("new_post", post)
//
//                setResult(RESULT_OK, intent)
//                finish()
//            } catch (e: Exception) {
//                // Handle any unexpected errors
//                Toast.makeText(this, "Failed to add post: ${e.message}", Toast.LENGTH_LONG).show()
//                e.printStackTrace() // Optional: Log the exception for debugging purposes
//            }
//
//
//
//
//
//        // Update the ViewModel with the new post
//
//            // Finish activity and return to HomeFragment
//            finish()
//        }

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
    private fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}

