package com.redgunner.novanews.network

import com.redgunner.novanews.models.category.Categories
import com.redgunner.novanews.models.comment.Comments
import com.redgunner.novanews.models.post.Post
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordpressApi {


    @GET("/wp-json/wp/v2/posts")
    suspend fun getPostsByCategories(
        @Query("categories") categories:Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("_embed") embed: Boolean
    ) :List<Post>



    @GET("/wp-json/wp/v2/categories?per_page=50")
    suspend fun getCategories():List<Categories>


    @GET("/wp-json/wp/v2/posts/{postId}?&_embed=true")
    suspend fun getPostById(
        @Path("postId") postId:Int
    ): Post


    @GET("/wp-json/wp/v2/comments")
    suspend fun getPostComments(
        @Query("post") postId: Int
    ):List<Comments>



    @GET("/wp-json/wp/v2/posts")
    suspend fun searchPost(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("_embed") embed: Boolean
    ):List<Post>

}