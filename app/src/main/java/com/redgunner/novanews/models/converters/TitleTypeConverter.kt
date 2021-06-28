package com.redgunner.novanews.models.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.redgunner.novanews.models.post.Title

class TitleTypeConverter {

    companion object{


        @TypeConverter
        @JvmStatic
        fun fromTitleToString(title: Title):String{

            val gson=Gson()

            val titleType= object : TypeToken<Title>() {}.type

           return gson.toJson(title,titleType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToTile(titleString:String):Title{
            val gson=Gson()

            val titleType= object : TypeToken<Title>() {}.type


            return gson.fromJson(titleString,titleType)
        }


    }
}