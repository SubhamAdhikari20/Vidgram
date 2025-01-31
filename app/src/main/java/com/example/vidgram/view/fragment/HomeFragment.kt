package com.example.vidgram.view.fragment

import android.content.Intent
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
import com.example.vidgram.model.StoryModel
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.repository.StoryRepositoryImpl
import com.example.vidgram.view.activity.StoryActivity
import com.example.vidgram.viewmodel.PostViewModel
import com.example.vidgram.viewmodel.StoryViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var postViewModel: PostViewModel
    private lateinit var postsList: MutableList<PostModel>
    private lateinit var storyList: MutableList<StoryModel>
    lateinit var postAdapter: PostAdapter
    private lateinit var storyAdapter: StoryAdapter


    lateinit var storyViewModel: StoryViewModel



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

       storyList = mutableListOf()
        val storyRepo = StoryRepositoryImpl() // Initialize the repository
        storyViewModel = StoryViewModel(storyRepo) // Initialize the ViewModel

        // Initialize the adapter with the current storyList
        storyAdapter = StoryAdapter(storyList) { story ->
            // Handle the click event, navigate to the next activity
            val intent = Intent(requireContext(), StoryActivity::class.java)
            intent.putExtra("story_id", story.storyId) // Pass story ID
            intent.putExtra("username", story.username) // Pass username
            intent.putExtra("timestamp", story.storyTimeStamp) // Pass timestamp

            intent.putExtra("story_image", story.storyImage) // Pass story image URL (or resource)
            startActivity(intent)
        }
        binding.recyclerViewStories.adapter = storyAdapter

        // Call to get all stories from the ViewModel
        storyViewModel.getAllStory()

        // Observe the LiveData for changes in stories
        storyViewModel.getAllStories.observe(viewLifecycleOwner) { stories ->
            // Update the storyList with new data
            storyList.clear() // Clear the existing list
            if (stories != null) {
                storyList.addAll(stories)
            } // Add new stories to the list
            storyAdapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }


        // Initialize RecyclerView for posts

        postsList = mutableListOf()
        postAdapter = PostAdapter(requireContext(), postsList)




        val recyclerView = binding.recyclerViewPosts  // Access RecyclerView directly from the binding
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter





        postViewModel.getAllPost()

// Set up an observer for the posts list
        postViewModel.getAllPosts.observe(viewLifecycleOwner, { posts ->
            // If posts is null, provide an empty list
            val postAdapter = PostAdapter(requireContext(), (posts ?: emptyList()).toMutableList())
            recyclerView.adapter = postAdapter
        })








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