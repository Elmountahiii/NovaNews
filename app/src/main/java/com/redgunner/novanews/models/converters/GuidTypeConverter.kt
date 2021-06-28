package com.redgunner.novanews.models.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.redgunner.novanews.models.post.Guid

class GuidTypeConverter {


    companion object {

        @TypeConverter
        @JvmStatic
        fun fromGuidToString(guid: Guid): String {
            val gson = Gson()

            val guidType = object : TypeToken<Guid>() {}.type


            return gson.toJson(guid, guidType)

        }

        @TypeConverter
        @JvmStatic
        fun fromStringToGuid(guid:String):Guid{
            val gson = Gson()

            val guidType = object : TypeToken<Guid>() {}.type


           return gson.fromJson(guid,guidType)
        }
    }
}