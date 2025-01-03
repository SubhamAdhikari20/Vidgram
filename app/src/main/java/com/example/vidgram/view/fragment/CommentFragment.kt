package com.example.vidgram.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vidgram.R
import com.example.vidgram.databinding.FragmentCommentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CommentFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.CommentDialogStyle) // Apply custom style
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(inflater, container, false)

        // Dismiss dialog when the horizontal bar is clicked (optional)
//        binding.linearLayout4.setOnClickListener {
//            dismiss()
//        }

        return binding.root
    }

    companion object {
        fun newInstance(): CommentFragment {
            return CommentFragment()
        }
    }
}
