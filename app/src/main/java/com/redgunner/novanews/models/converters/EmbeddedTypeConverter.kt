package com.redgunner.novanews.models.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.redgunner.novanews.models.post.Embedded

class EmbeddedTypeConverter {


    companion object{

        @TypeConverter
        @JvmStatic
        fun fromEmbeddedToString(embedded: Embedded):String{
            val gson = Gson()
            val embeddedType = object : TypeToken<Embedded>() {}.type

            return gson.toJson(embedded, embeddedType)
        }



        @TypeConverter
        @JvmStatic
        fun fromStringToEmbedded(embeddedString: String):Embedded{

            val gson = Gson()
            val embeddedType = object : TypeToken<Embedded>() {}.type

            return gson.fromJson(embeddedString, embeddedType)

        }

    }
}