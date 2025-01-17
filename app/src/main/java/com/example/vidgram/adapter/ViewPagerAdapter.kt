package com.example.vidgram.adapter


import android.graphics.drawable.Icon
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vidgram.view.fragment.PhotosMyProfileFragment
import com.example.vidgram.view.fragment.VideosMyProfileFragment

class ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return PhotosMyProfileFragment()
            1 -> return VideosMyProfileFragment()
            else -> return PhotosMyProfileFragment()
        }
    }

}


/*
class ViewPagerAdapter (fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragmentList = mutableListOf<Fragment>()
//    val titleList = mutableListOf<String>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return titleList.get(position)
//    }


    fun addFragment(fragment: Fragment, title: String){
        fragmentList.add(fragment)
//        titleList.add(title)
    }

}
 */