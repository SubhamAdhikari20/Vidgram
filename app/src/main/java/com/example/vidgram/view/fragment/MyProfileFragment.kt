package com.example.vidgram.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isEmpty
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentMyProfileBinding
import com.example.vidgram.adapter.ViewPagerAdapter
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.view.activity.EditProfileActivity
import com.example.vidgram.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private var photoFile: File? = null // Store the file created for the image


    private var icons = arrayOf(
        R.drawable.photo_icon,
        R.drawable.video_icon,
    )

    var data = arrayOf(
        "Active Order",
        "Cancel Order",
        "Delivered Order",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the toolbar in the hosting activity
//        val activity = activity as? AppCompatActivity
//        activity?.setSupportActionBar(binding.toolbar)
//        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)

        // Configure the Toolbar
        if (binding.toolbar.menu.isEmpty()){
            binding.toolbar.inflateMenu(R.menu.profile_toolbar)
        }
        else{
//            TODO("Menu Already Implemented")
        }

        binding.toolbar.title = ""
        binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_arrow_resized)
        binding.toolbar.setNavigationOnClickListener {
            // Handle the back button click
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Handle menu item clicks
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profileMenuOptionButton -> {
                    val intent = Intent(requireContext(), EditProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        val currentUser = userViewModel.getCurrentUser()
        currentUser.let{    // it -> currentUser
            userViewModel.getUserFromDatabase(it?.uid.toString())
        }


        userViewModel.userData.observe(requireActivity()){
            binding.nameTextView.text = it?.fullName.toString()

        }

        val fragmentManager: FragmentManager = childFragmentManager
        viewPagerAdapter = ViewPagerAdapter(fragmentManager, lifecycle)
        binding.myProfileViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.myProfileTableLayout, binding.myProfileViewPager) {
//            tabs, position -> tabs.text = data[position]
                tabs, position ->
            tabs.icon = resources.getDrawable(icons[position], null)
        }.attach()

        binding.editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.profileImage.setImageURI(it)
                binding.profileImage.visibility = View.VISIBLE
            }
        }

        /*
        // Initialize the camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && photoFile != null) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    binding.profileImage.setImageBitmap(it)
                    binding.profileImage.visibility = View.VISIBLE
                }
            }
            else {
                Toast.makeText(requireContext(), "Camera action canceled!", Toast.LENGTH_SHORT).show()
            }
        }
         */

        // Initialize the camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.fileprovider",
                    photoFile!!
                )

                val imageBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, photoUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, photoUri)
                }
                binding.profileImage.setImageBitmap(imageBitmap)
                binding.profileImage.visibility = View.VISIBLE
            }
            else {
                Toast.makeText(requireContext(), "Camera action canceled!", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize the permissions launcher
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                openImagePickerDialog()
            } else {
                Toast.makeText(requireContext(), "Permissions required to proceed!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Add Image" button click
        binding.profileImage.setOnClickListener{
            checkPermissionsAndOpenPicker()
        }


        


        /*
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragment(PhotosMyProfileFragment(), "Photo")
        viewPagerAdapter.addFragment(VideosMyProfileFragment(), "Video")
        binding.myProfileViewPager.adapter = viewPagerAdapter
        binding.myProfileTableLayout.setupWithViewPager(binding.myProfileViewPager)

        // Set custom icons for each tab
        binding.myProfileTableLayout.getTabAt(0)?.setIcon(R.drawable.photo_icon)
        binding.myProfileTableLayout.getTabAt(1)?.setIcon(R.drawable.video_icon)
        */

    }

    // Function to check and request permissions
    private fun checkPermissionsAndOpenPicker() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        if (requiredPermissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }) {
            openImagePickerDialog()
        }
        else {
            permissionsLauncher.launch(requiredPermissions)
        }
    }

    private fun openImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Take a Photo")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> { // Open Gallery
                    imagePickerLauncher.launch("image/*")
                }
                1 -> { // Open Camera
                    openCameraWithFile()
                    /*
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                        cameraLauncher.launch(takePictureIntent)
                    }
                    else {
                        Toast.makeText(requireContext(), "Camera not available!", Toast.LENGTH_SHORT).show()
                    }

                     */
                }
            }
        }
        builder.show()
    }

    private fun openCameraWithFile() {
        photoFile = createImageFile() // Create a file for storing the image
        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile!!
        )

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
        } else {
            Toast.makeText(requireContext(), "Camera not available!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}