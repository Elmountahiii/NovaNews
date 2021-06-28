package com.redgunner.novanews.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redgunner.novanews.models.post.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPostsDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(post:Post)


    @Query("SELECT * FROM LocalPost ")
    fun getFavoriteAccounts(): Flow<List<Post>>


    @Query("SELECT * FROM LocalPost WHERE id =:postId")
   suspend fun getLocalPost(postId:Int): Post?

}