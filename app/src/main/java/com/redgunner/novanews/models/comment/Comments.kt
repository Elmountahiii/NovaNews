package com.redgunner.novanews.models.comment

data class Comments(
    val author_name: String,
    val content: String,
    val date: String,
    val id: Int,
    val post: Int
)