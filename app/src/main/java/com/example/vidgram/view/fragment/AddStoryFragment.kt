package com.example.vidgram.view.fragment
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vidgram.R
import java.io.IOException

class AddStoryFragment : Fragment() {

    private lateinit var chooseMediaButton: Button
    private lateinit var addStoryButton: Button
    private lateinit var mediaPreview: ImageView
    private var selectedMediaUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_story, container, false)

        chooseMediaButton = view.findViewById(R.id.chooseMediaButton)
        addStoryButton = view.findViewById(R.id.addStoryButton)
        mediaPreview = view.findViewById(R.id.mediaPreview)

        // Set button listeners
        chooseMediaButton.setOnClickListener { openGallery() }
        addStoryButton.setOnClickListener { addStory() }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/* video/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedMediaUri = data?.data
            if (selectedMediaUri != null) {
                mediaPreview.visibility = View.VISIBLE
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver, selectedMediaUri
                    )
                    mediaPreview.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun addStory() {
        if (selectedMediaUri == null) {
            Toast.makeText(context, "Please select a media file", Toast.LENGTH_SHORT).show()
            return
        }

        // Simulate uploading the story
        Toast.makeText(context, "Story added successfully!", Toast.LENGTH_SHORT).show()

        // Pass the selected story back to the display fragment
        requireActivity().supportFragmentManager.popBackStack()
    }
}
