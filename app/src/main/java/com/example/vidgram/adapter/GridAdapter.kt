package com.example.vidgram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vidgram.R
import com.example.vidgram.databinding.GridItemBinding
import com.example.vidgram.model.GridItem

class GridAdapter(private val photoList: List<GridItem>) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    class GridViewHolder(val binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)  // ✅ Correct binding initialization
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = photoList[position]

        // Use a default placeholder if the image is null or empty
        val imageUrl = item.imageUrl.takeIf { !it.isNullOrBlank() }
            ?: "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRq5X-65GJP2-k8xiPzjUYjEtZhJKqYvzkt-3BeAo69t3LXeUJouOgJBLk55Iuirlu3ftWZcoC6lsIZT5_VWuj_AVdNLU93kqhEE2xgYw8"  // Replace with a valid placeholder URL

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.person)  // Ensure you have a `placeholder.png` in `res/drawable`
            .error(R.drawable.person1)
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = photoList.size
}
