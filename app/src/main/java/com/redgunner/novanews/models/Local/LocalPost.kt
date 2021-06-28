package com.redgunner.novanews.models.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.redgunner.novanews.models.post.Title


data class LocalPost(
    @PrimaryKey
    val id: Int,
    val title: String,
    val content: String,
    val date: String,
    val category: String,
    val postUrl:String,
    val imageUrl:String
)
