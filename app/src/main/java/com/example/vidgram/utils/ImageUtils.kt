package com.example.vidgram.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat


class ImageUtils(private val activity: AppCompatActivity) {
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionGalleryLauncher: ActivityResultLauncher<String>
    private lateinit var permissionCameraLauncher: ActivityResultLauncher<String>

    fun registerActivity(galleryCallback: (Uri?) -> Unit, cameraCallback: (Uri?) -> Unit) {
        // Register gallery launcher
        galleryLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val imageData = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && imageData != null) {
                val imageUri = imageData.data
                galleryCallback(imageUri) // Return the selected image URI
            } else {
                Log.e("ImageUtils", "Image selection failed or cancelled")
            }
        }

        // Register camera launcher
        cameraLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val imageData = result.data
            if (result.resultCode == AppCompatActivity.RESULT_OK && imageData != null) {
                val imageUri = imageData.data
                cameraCallback(imageUri) // Return the captured image URI
            } else {
                Log.e("ImageUtils", "Camera capture failed or cancelled")
            }
        }

        // Register gallery permission launcher for storage access
        permissionGalleryLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            }
            else {
                Log.e("ImageUtils", "Permission denied for gallery access")
            }
        }

        // Register camera permission launcher for storage access
        permissionCameraLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            }
            else {
                Log.e("ImageUtils", "Permission denied for camera access")
            }
        }
    }

    fun launchGallery(context: Context) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        }
        else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionGalleryLauncher.launch(permission)
        }
        else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply{
            type = "image/*"}
        galleryLauncher.launch(intent)
    }



    fun launchCamera(context: Context){
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.CAMERA
        }
        else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionCameraLauncher.launch(permission)
        }
        else {
            openCamera()
           }
    }

    private fun openCamera(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(takePictureIntent)
    }

    /*
    private fun openCamera(callback: (Boolean, String?) -> Unit){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
            callback(true, null)
        }
        else {
            callback(false, "Camera not available!")
        }
    }

     */


}