package com.example.vidgram.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.vidgram.R
import com.example.vidgram.databinding.ActivityBottomNavigationBinding
import com.example.vidgram.view.fragment.CommunityFragment
import com.example.vidgram.view.fragment.HomeFragment
import com.example.vidgram.view.fragment.MessageFragment
import com.example.vidgram.view.fragment.NotificationFragment
import com.example.vidgram.view.fragment.PostFragment

class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBottomNavigationBinding

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
                        R.id.navPost -> replaceFragment(PostFragment())
                            R.id.navCommunity -> replaceFragment(CommunityFragment())
                                R.id.navNotification -> replaceFragment(NotificationFragment())
                                    else -> {}
            }
            true
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}