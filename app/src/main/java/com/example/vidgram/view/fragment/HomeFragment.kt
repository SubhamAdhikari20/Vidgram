package com.example.vidgram.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentHomeBinding
import com.example.vidgram.model.Post
import com.example.vidgram.model.Story
import com.example.vidgram.adapter.StoryAdapter
import com.example.vidgram.adapter.PostAdapter
import com.example.vidgram.model.PostModel
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.viewmodel.PostViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val posts = mutableListOf<Post>()

    lateinit var postViewModel: PostViewModel


    private lateinit var postsList: MutableList<PostModel>

    // Story Recycler View
    private val storyImageList: ArrayList<Int> = ArrayList()
    private val storyNameList: ArrayList<String> = ArrayList()
//    private lateinit var storyAdapter : StoryRecyclerViewAdapter
lateinit var postAdapter: PostAdapter

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

        // Initialize postAdapter with an empty mutable list or with existing posts
        val repo = PostRepositoryImpl()
        postViewModel =PostViewModel(repo)

// Later when you need to update the list, you can call:

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



        /*
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
        */



        postsList = mutableListOf()
        postAdapter = PostAdapter(requireContext(), postsList){ post ->
            // Handle comment click
            openCommentDialog(post)
        }

        val recyclerView = binding.recyclerViewPosts  // Access RecyclerView directly from the binding
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter




        postViewModel.getAllPost()

// Set up an observer for the posts list
        postViewModel.getAllPosts.observe(viewLifecycleOwner, { posts ->
            // If posts is null, provide an empty list
            val postAdapter = PostAdapter(requireContext(), (posts ?: emptyList()).toMutableList()){ post ->
                // Handle comment click
                openCommentDialog(post)
            }
            recyclerView.adapter = postAdapter
        })

    }



    private fun openCommentDialog(post: PostModel) {
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
