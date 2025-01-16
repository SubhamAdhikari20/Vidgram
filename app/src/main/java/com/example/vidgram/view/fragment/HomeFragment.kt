package com.example.vidgram.view.fragment

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentHomeBinding
import com.example.vidgram.model.Post
import com.example.vidgram.model.Story
import com.example.vidgram.adapter.StoryAdapter
import com.example.vidgram.adapter.PostAdapter

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    // Story Recycler View
    private val storyImageList: ArrayList<Int> = ArrayList()
    private val storyNameList: ArrayList<String> = ArrayList()
//    private lateinit var storyAdapter : StoryRecyclerViewAdapter

    // Post Feed Recycler View
    private val postImageList: ArrayList<Int> = ArrayList()
    private val postAvaterImageList: ArrayList<Int> = ArrayList()
    private val postNameList: ArrayList<String> = ArrayList()
    private val messageList: ArrayList<String> = ArrayList()
//    private lateinit var postFeedAdapter : PostFeedRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set up the toolbar in the hosting activity
//        val activity = activity as? AppCompatActivity
//        activity?.setSupportActionBar(binding.toolbar)
//        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)


        // Configure the Toolbar
//        binding.toolbar.title = "" // Remove default title if needed
//        binding.toolbar.inflateMenu(R.menu.home_page_toolbar) // Inflate the menu directly into the Toolbar

        // Add a back button
//        binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_arrow_resized)
//        binding.toolbar.setNavigationOnClickListener {
//            // Handle the back button click
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }

//        // Handle menu item clicks
//        binding.toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.searchMenuButton -> {
//                    replaceFragment(SearchFragment())
//                    true
//                }
//                R.id.profileMenuButton -> {
//                    replaceFragment(MyProfileFragment())
//                    true
//                }
//                else -> false
//            }
//        }



        // Attach MenuProvider to handle the menu
        binding.toolbar.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (binding.toolbar.menu.isEmpty()){
                    menuInflater.inflate(R.menu.home_page_toolbar, menu)
                }
                else{
//                    TODO("Menu Already Implemented")
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.searchMenuButton -> {
                        // Handle search button click
                        replaceFragment(SearchFragment())
                        true
                    }
                    R.id.profileMenuButton -> {
                        // Navigate to profile fragment
                        replaceFragment(MyProfileFragment())
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        // Navigate to profile fragment on button click
//        binding.profileButton.setOnClickListener {
//            replaceFragment(MyProfileFragment())
//        }


        // Add Story Button Logic
//        binding.addStoryImage.setOnClickListener {
//            openAddStoryFragment()
//        }

        // Initialize RecyclerView for stories
        binding.recyclerViewStories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val stories = listOf(
            Story("John", R.drawable.person1),
            Story("Alice", R.drawable.person1),
            Story("Bob", R.drawable.person1),
            Story("Emma", R.drawable.person1),
            Story("John", R.drawable.person1),
            Story("Alice", R.drawable.person1),
            Story("Bob", R.drawable.person1),
            Story("Emma", R.drawable.person1),
            Story("John", R.drawable.person1),
            Story("Alice", R.drawable.person1),
            Story("Bob", R.drawable.person1),
            Story("Emma", R.drawable.person1)
        )

        val storyAdapter = StoryAdapter(stories)
        binding.recyclerViewStories.adapter = storyAdapter

        // Initialize RecyclerView for posts
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())

        val posts = listOf(
            Post("John Doe", R.drawable.my_story_icon, R.drawable.person1, "Enjoying the sunset!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Alice Smith", R.drawable.my_story_icon, R.drawable.person1, "Had a great day!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Bob Lee", R.drawable.my_story_icon, R.drawable.person1, "Coffee break!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Emma Brown", R.drawable.my_story_icon, R.drawable.person1, "Amazing hike!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("John Doe", R.drawable.my_story_icon, R.drawable.person1, "Enjoying the sunset!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Alice Smith", R.drawable.my_story_icon, R.drawable.person1, "Had a great day!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Bob Lee", R.drawable.my_story_icon, R.drawable.person1, "Coffee break!", "12:00", "24k", "1k", "1,080", "2.4k"),
            Post("Emma Brown", R.drawable.my_story_icon, R.drawable.person1, "Amazing hike!", "12:00", "24k", "1k", "1,080", "2.4k")
        )

        val postAdapter = PostAdapter(posts) { post ->
            // Handle comment click
            openCommentDialog(post)
        }

        binding.recyclerViewPosts.adapter = postAdapter
    }



    private fun openCommentDialog(post: Post) {
        val commentDialog = CommentFragment() // Replace with your dialog fragment
        val bundle = Bundle()
//        bundle.putParcelable("post", post) // Pass the clicked post object
        commentDialog.arguments = bundle
        commentDialog.show(parentFragmentManager, "CommentFragment")
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun openAddStoryFragment() {
        replaceFragment(AddStoryFragment())
    }
}
