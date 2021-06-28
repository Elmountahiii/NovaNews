package com.redgunner.novanews.models.post

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.redgunner.novanews.models.converters.ContentTypeConverter
import com.redgunner.novanews.models.converters.EmbeddedTypeConverter
import com.redgunner.novanews.models.converters.GuidTypeConverter
import com.redgunner.novanews.models.converters.TitleTypeConverter

@Entity(tableName = "LocalPost")
data class Post(
    @TypeConverters(EmbeddedTypeConverter::class)
    val _embedded: Embedded,
    @TypeConverters(ContentTypeConverter::class)
    val content: Content,
    val date: String,
    @PrimaryKey
    val id: Int,
    @TypeConverters(TitleTypeConverter::class)
    val title: Title,
    @TypeConverters(GuidTypeConverter::class)
    val guid:Guid
)