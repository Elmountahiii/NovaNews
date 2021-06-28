package com.redgunner.novanews.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.redgunner.novanews.database.dao.SavedPostsDAO
import com.redgunner.novanews.models.post.Post
import com.redgunner.novanews.network.WordpressApi
import com.redgunner.novanews.paging.PostPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordPressRepository @Inject constructor(
    private val wordpressApi: WordpressApi,
    private val savedPostsDAO: SavedPostsDAO
) {



    val savedPosts=savedPostsDAO.getFavoriteAccounts()


    fun getPostByCategory(CategoryId: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            PostPagingSource(
                categoryId = CategoryId,
                wordpressApi = wordpressApi
            )
        }
    ).liveData


    suspend fun getCategories() = wordpressApi.getCategories()


    suspend fun getPostById(postId: Int) :Post{


        return savedPostsDAO.getLocalPost(postId) ?: wordpressApi.getPostById(postId = postId)

    }

    suspend fun getPostComments(postId: Int) = wordpressApi.getPostComments(postId = postId)

    suspend fun searchPost(query: String) =
        wordpressApi.searchPost(search = query, page = 1, perPage = 18, embed = true)


    suspend fun addPostToDatabase(post: Post) {
        try {

           savedPostsDAO.addPost(post)

        }catch (exception :Exception){

        }
    }








}