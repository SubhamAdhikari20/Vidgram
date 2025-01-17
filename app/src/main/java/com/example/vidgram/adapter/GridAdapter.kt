package com.example.vidgram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.vidgram.R

class GridAdapter : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private val items = listOf(
        R.drawable.husky, // Replace with actual drawable resources
        R.drawable.person1,
        R.drawable.profile_pic,
        R.drawable.husky, // Replace with actual drawable resources
        R.drawable.person1,
        R.drawable.profile_pic,
        R.drawable.husky, // Replace with actual drawable resources
        R.drawable.person1,
        R.drawable.profile_pic,
        R.drawable.husky, // Replace with actual drawable resources
        R.drawable.person1,
        R.drawable.profile_pic,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item, parent, false)


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
