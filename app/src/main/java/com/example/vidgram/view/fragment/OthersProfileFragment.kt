package com.example.vidgram.view.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.adapter.ProfilePagerAdapter
import com.example.vidgram.databinding.FragmentOthersProfileBinding
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.repository.ChatRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.view.activity.MessageActivity
import com.example.vidgram.viewmodel.ChatViewModel
import com.example.vidgram.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class OthersProfileFragment : Fragment() {
    lateinit var binding : FragmentOthersProfileBinding
    lateinit var chatViewModel: ChatViewModel
    lateinit var userViewModel: UserViewModel
    var receiverId: String ?= null
    var icons = arrayOf(R.drawable.photo_icon, R.drawable.video_icon)
    var chatModel = UserChatInfo()
    var bundle = Bundle()
    lateinit var user1Id: String
    lateinit var user2Id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var chatRepositoryImpl = ChatRepositoryImpl()
        chatViewModel = ChatViewModel(chatRepositoryImpl)
        receiverId = arguments?.getString("user2Id")

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


        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)

        val currentUser = userViewModel.getCurrentUser()
        currentUser.let{    // it -> currentUser
//            Log.d("userId",it?.uid.toString())
            user1Id = it?.uid.toString()
            userViewModel.getUserFromDatabase(it?.uid.toString())
        }

        arguments?.let {
            user2Id = it.getString("user2Id").toString()
        }

        Log.d("user2Id", user2Id)
        var fullName = binding.othersProfileName
        var profilePicture : String ?= null
        var profileCoverImage : String ?= null

        userViewModel.getUserFromDatabase(user2Id)
        userViewModel.userData.observe(requireActivity()){
            fullName.text = it?.fullName.toString()
            if (it?.profilePicture.isNullOrEmpty()) {
                Glide.with(this).load(R.drawable.user).circleCrop().into(binding.profilePicture)
            }
            else{
                Glide.with(requireContext()).load(it?.profilePicture).circleCrop().into(binding.profilePicture)
            }

            if (it?.profileCoverImage.isNullOrEmpty()){
                Glide.with(requireContext()).load(R.drawable.cover_profile).into(binding.profileCoverImage)

            }
            else{
                Glide.with(requireContext()).load(it?.profileCoverImage).into(binding.profileCoverImage)

            }

            profilePicture = it?.profilePicture
            profileCoverImage = it?.profileCoverImage
        }

        binding.messageButton.setOnClickListener {
//            chatModel.senderId = "GI9tjJAB9JfHYfye0AhTNIJX7j32"    // Deepak Malla
//            chatModel.receiverId = "v4xWlr2zR6hwoXG4QhezWAUnHmx1"   // Yogesh Chaudhary
            val intent = Intent(requireContext(), MessageActivity::class.java).apply {

                putExtra("chatId", chatModel.chatId)
                putExtra("receiverId", user2Id)
                putExtra("fullName", fullName.text.toString())
                putExtra("profilePicture", profilePicture)
            }

            startActivity(intent)
        }

        val adapter = ProfilePagerAdapter(this)  // Pass the fragment as context
        binding.otherProfileViewPager.adapter = adapter

        // Set up TabLayout with icons
        TabLayoutMediator(binding.otherProfileTableLayout, binding.otherProfileViewPager) { tabs, position ->
            tabs.icon = resources.getDrawable(icons[position], null)
        }.attach()

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