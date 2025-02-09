package com.example.vidgram.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
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
import androidx.exifinterface.media.ExifInterface
import com.cloudinary.android.MediaManager
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityNewPostBinding
import com.example.vidgram.model.PostModel
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.utils.ImageUtils
import com.example.vidgram.utils.LoadingDialogUtils
import com.example.vidgram.viewmodel.PostViewModel
import com.example.vidgram.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var userViewModel: UserViewModel
    lateinit var postViewModel: PostViewModel
    lateinit var loadingDialogUtils: LoadingDialogUtils
    lateinit var imageUtils: ImageUtils
    var imageUri: Uri? = null


    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialogUtils = LoadingDialogUtils(this)

        // User Backend Binding
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)

        // Post Backend Binding
        val postRepo = PostRepositoryImpl()
        postViewModel = PostViewModel(postRepo)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity (
            galleryCallback = { uri ->
                uri?.let {
                    imageUri = it
                    val rotation = getCorrectRotation(this, it)
                    Picasso.get().load(it).rotate(rotation.toFloat()).into(binding.postImageView)
//                    binding.postImageView.setImageURI(imageUri)
                    binding.postImageView.visibility = View.VISIBLE
                    updatePostButtonState(true)
                    Log.d("Gallery", "Image selected: $it")
                }
            },

            cameraCallback = { uri ->
                uri?.let {
                    imageUri = it
                    Picasso.get().load(it).into(binding.postImageView)
                    binding.postImageView.visibility = View.VISIBLE
                    updatePostButtonState(true)
                    Log.d("Camera", "Photo taken: $it")
                }
            }
        )

        /*
        val config = mutableMapOf<String, String>()
        config["cloud_name"] = "dbukovsi1"
        config["api_key"] = "718742783263144"
        config["api_secret"] = "udtElGelBPWkalRKw-RQrnqFRI8"

        MediaManager.init(this, config)
        */

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

        /*
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
//                    binding.postImageView.visibility = android.view.View.VISIBLE
                    updatePostButtonState(true)
                }
            }
        }
         */

        /*
        // Initialize the permissions launcher
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                openImagePickerDialog()
            } else {
                Toast.makeText(this, "Permissions required to proceed!", Toast.LENGTH_SHORT).show()
            }
        }
         */

        // Handle "Add Image" button click
        binding.addPostImageView.setOnClickListener {
            openImagePickerDialog()
//            checkPermissionsAndOpenPicker()
        }


        // Post Button
        binding.postButton.setOnClickListener {
            if(imageUri == null) {
                addPost("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png")
            }
            else{
                uploadPostImage()
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

    private fun addPost(url: String){
        var postDesc : String? = binding.postDescEditTextField.text.toString()
        var postBy : String? = binding.newPostUserName.text .toString()
        var postTimeStamp : String? = binding.newPostFeedTime.text .toString()
//        val profileImageUrl: String? = binding.newPostProfileImage.toString()

        var postModel = PostModel(postId = "", postImageUrl = url, postDescription = postDesc, postBy = postBy, postTimeStamp = postTimeStamp)

        postViewModel.addPost(postModel){
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

    private fun uploadPostImage(){
        loadingDialogUtils.show()
        imageUri?.let { uri ->
            postViewModel.uploadPostImage(this, uri) { imageUrl ->
                Log.d("checkpoints", imageUrl.toString())
                if (imageUrl != null) {
                    addPost(imageUrl)
                }
                else {
                    Toast.makeText(this@NewPostActivity, "Failed to upload image to Cloudinary", Toast.LENGTH_LONG).show()
                    Log.e("Upload Error", "Failed to upload image to Cloudinary")
                }
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
                    imageUtils.launchGallery(this)
//                    imagePickerLauncher.launch("image/*")
                }
                1 -> { // Open Camera
                    imageUtils.launchCamera(this)
//                        cameraLauncher.launch(takePictureIntent)

                }
            }
        }
        builder.show()
    }

    private fun getCorrectRotation(context: Context, imageUri: Uri): Int {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val exif = ExifInterface(inputStream!!)
            inputStream.close()

            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


}