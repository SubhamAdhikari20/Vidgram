package com.example.vidgram.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentSearchBinding



class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding


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

        // Set up the toolbar in the hosting activity
//        val activity = activity as? AppCompatActivity
//        activity?.setSupportActionBar(binding.toolbar)
//        activity?.supportActionBar?.setDisplayShowTitleEnabled(false)
//        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        activity?.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow_resized)

        binding.toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.back_arrow_resized)
        binding.toolbar.setNavigationOnClickListener {
//            // Handle the back button click
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }



    }








}