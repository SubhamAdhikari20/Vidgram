package com.example.vidgram.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.adapter.GridAdapter
import com.example.vidgram.databinding.FragmentPhotosMyProfileBinding
import com.example.vidgram.model.GridItem
import com.example.vidgram.model.ContentType
import com.example.vidgram.repository.PostRepositoryImpl
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.viewmodel.PostViewModel
import com.example.vidgram.viewmodel.UserViewModel

class PhotosMyProfileFragment : Fragment() {
    lateinit var binding: FragmentPhotosMyProfileBinding

    private lateinit var userViewModel: UserViewModel
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosMyProfileBinding.inflate(inflater, container, false)

        // Initialize ViewModels
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)
        postViewModel = PostViewModel(PostRepositoryImpl())

        val currentUser = "GI9tjJAB9JfHYfye0AhTNIJX7j32"
        userViewModel.getUserFromDatabase(currentUser)

        // Fetch posts and observe
        postViewModel.getAllPost()
        postViewModel.getAllPosts.observe(viewLifecycleOwner) { posts ->
            val filteredPosts = posts?.filter { post ->
                post.username == "Subahm"  // Only keep posts from the current user
            }?.map { post ->
                GridItem(ContentType.IMAGE, imageUrl = post.postImageUrl ?: "")
            } ?: emptyList()

            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = GridAdapter(filteredPosts)
            }
        }


        return binding.root
    }
}
