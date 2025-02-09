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
    lateinit var postViewModel: PostViewModel
    lateinit var postAdapter: PostAdapter

    var bundle = Bundle()
//    var postModelList = ArrayList<PostModel>()


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
        binding.recyclerViewStories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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


        postViewModel.getAllPost()
        setupObservers()

        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        postAdapter = PostAdapter(requireContext(), ArrayList()){ post ->
            // Handle comment click
            openCommentDialog(post)
        }

        binding.recyclerViewPosts.adapter = postAdapter


    }

    fun openCommentDialog(post: PostModel) {
        val commentDialog = CommentFragment() // Replace with your dialog fragment
        val bundle = Bundle()
//        bundle.putParcelable("post", post) // Pass the clicked post object
        commentDialog.arguments = bundle
        commentDialog.show(parentFragmentManager, "CommentFragment")
    }


    fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    fun openAddStoryFragment() {
        replaceFragment(AddStoryFragment())
    }

    fun setupObservers() {
        postViewModel.getAllPosts.observe(requireActivity()){ posts ->
            posts?.let {
                postAdapter.updateData(posts)
            }
        }
//        binding.recyclerViewPosts.smoothScrollToPosition(postModelList.size - 1)

        postViewModel.loadingAllPost.observe(requireActivity()) { isLoading ->
            // Handle loading state if needed
        }
    }

}
