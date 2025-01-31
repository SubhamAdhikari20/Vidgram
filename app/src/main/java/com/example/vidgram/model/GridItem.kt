package com.example.vidgram.model

data class GridItem (
    val type: ContentType,

    val imageUrl: String ,// Assuming you're using a URL for the image
    val videoUrl: String? = null // Video URL for videos
)
enum class ContentType {
    IMAGE,
    VIDEO
}