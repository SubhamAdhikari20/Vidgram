package com.example.vidgram.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vidgram.R
import com.example.vidgram.adapter.SearchUserAdapter
import com.example.vidgram.databinding.FragmentSearchBinding
import com.example.vidgram.model.UserChatInfo
import com.example.vidgram.model.UserModel
import com.example.vidgram.repository.UserRepositoryImpl
import com.example.vidgram.viewmodel.UserViewModel
import java.util.Locale


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var searchUserAdapter: SearchUserAdapter
    lateinit var userViewModel: UserViewModel
    var userModelList = ArrayList<UserModel>()
    var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_arrow_resized)
        binding.toolbar.setNavigationOnClickListener {
            // Handle the back button click
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // User Backend Binding
        val userRepo = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepo)

        // Fetch user data from Firebase
        setupObservers()
        userViewModel.getAllUsers(){
            users, success, messages ->
            if (users != null) {
                userModelList = users
            }
        }
//        Log.d("userIdImpl4", userId)

        // Initialize RecyclerView and Adapter
        searchUserAdapter = SearchUserAdapter(requireContext(), userModelList) { user ->
            val othersProfileFragment = OthersProfileFragment()
            // navigate to chat
            bundle.putString("user2Id", user.userID)
            othersProfileFragment.arguments = bundle
            replaceFragment(othersProfileFragment)
        }

        binding.searchRecyclerView.adapter = searchUserAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEditText.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })


    }

    private fun filterList(query : String?){
        if (query != null){
            var filteredList = ArrayList<UserModel>()
            for (userModel in userModelList){
                if (userModel.fullName?.toLowerCase(Locale.ROOT)?.contains(query) == true){
                    filteredList.add(userModel)
                }
            }

            if (filteredList.isEmpty()){
                Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
            }
            else{
                searchUserAdapter.setFilteredList(filteredList)
            }
        }
    }

    private fun setupObservers() {
        userViewModel.getAllusers.observe(requireActivity()){ messages ->
            messages?.let {
                searchUserAdapter.updateData(messages)
            }
            binding.searchRecyclerView.smoothScrollToPosition(userModelList.size - 1)
        }

        userViewModel.loadingAllUsers.observe(requireActivity()) { isLoading ->
            // Handle loading state if needed
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }






}