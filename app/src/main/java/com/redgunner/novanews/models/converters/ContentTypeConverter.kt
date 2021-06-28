package com.redgunner.novanews.models.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.redgunner.novanews.models.post.Content

class ContentTypeConverter {

    companion object{


        @TypeConverter
        @JvmStatic
        fun fromContentToString(content: Content): String {
            val gson = Gson()


            val contentType = object : TypeToken<Content>() {}.type

            return gson.toJson(content, contentType)
        }


        @TypeConverter
        @JvmStatic
        fun fromStringToContentObject(contentString:String):Content{
            val gson = Gson()

            val contentType = object : TypeToken<Content>() {}.type


            return gson.fromJson(contentString,contentType)

        }


    }

}