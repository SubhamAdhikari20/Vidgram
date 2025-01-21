package com.example.vidgram.view.activity

import android.content.Intent
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityBottomNavigationBinding
import com.example.vidgram.model.Post
import com.example.vidgram.view.fragment.CommunityFragment
import com.example.vidgram.view.fragment.HomeFragment
import com.example.vidgram.view.fragment.MessageFragment
import com.example.vidgram.view.fragment.NotificationFragment
import com.example.vidgram.view.fragment.AddPostFragment
import com.example.vidgram.viewmodel.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var postViewModel: PostViewModel
    private lateinit var binding : ActivityBottomNavigationBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    private val ADD_POST_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { menu ->
            when(menu.itemId){
                R.id.navHome -> replaceFragment(HomeFragment())
                    R.id.navMessage -> replaceFragment(MessageFragment())
                        R.id.navPost -> showAddPostDialog()
                            R.id.navCommunity -> replaceFragment(CommunityFragment())
                                R.id.navNotification -> replaceFragment(NotificationFragment())
                                    else -> {}
            }
            true
        }
        
        // In BottomNavigationActivity
//        postViewModel.posts.observe(this, Observer { updatedPosts ->
//            val homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName) as? HomeFragment
//            homeFragment?.postAdapter?.updatePosts(updatedPosts)  // Update HomeFragment's adapter
//        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottomNavigationLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun showAddPostDialog() {
        val intent = Intent(this, NewPostActivity::class.java)
        startActivityForResult(intent, ADD_POST_REQUEST_CODE)
//        val addPostFragment =  AddPostFragment()
//        addPostFragment.show(supportFragmentManager, "AddPostFragment")
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
//        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
//            R.anim.goback_slide_in_right, // Enter animation
//            R.anim.goback_slide_out_left, // Exit animation
//            R.anim.goback_slide_in_left,  // Pop enter animation (back)
//            R.anim.goback_slide_out_right // Pop exit animation (back)
//        )

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == ADD_POST_REQUEST_CODE && resultCode == RESULT_OK) {
//            val newPost = data?.getParcelableExtra<Post>("new_post")
//            newPost?.let {
//                // Pass post to HomeFragment's PostAdapter
//                val homeFragment =
//                    supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName) as? HomeFragment
//                homeFragment?.addPostToAdapter(it)
//            }
//        }
//
//
//    }
}