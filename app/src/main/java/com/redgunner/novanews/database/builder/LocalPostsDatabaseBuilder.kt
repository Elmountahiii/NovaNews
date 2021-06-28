package com.redgunner.novanews.database.builder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.redgunner.novanews.database.dao.SavedPostsDAO
import com.redgunner.novanews.models.converters.ContentTypeConverter
import com.redgunner.novanews.models.converters.EmbeddedTypeConverter
import com.redgunner.novanews.models.converters.GuidTypeConverter
import com.redgunner.novanews.models.converters.TitleTypeConverter
import com.redgunner.novanews.models.post.Post


@Database(entities = [Post::class], version = 1)
@TypeConverters(
    ContentTypeConverter::class,
    GuidTypeConverter::class,
    TitleTypeConverter::class,
    EmbeddedTypeConverter::class
)
abstract class LocalPostsDatabaseBuilder :RoomDatabase(){


    abstract fun savedPostsDao(): SavedPostsDAO

    companion object{


        private var Instance: LocalPostsDatabaseBuilder? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = Instance
            ?: synchronized(LOCK) {

                Instance
                    ?: buildDatabase(
                        context
                    )
                        .also { Instance = it }


            }




        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            LocalPostsDatabaseBuilder::class.java, "LocalPosts.db"
        ).fallbackToDestructiveMigration()
            .build()
    }




}