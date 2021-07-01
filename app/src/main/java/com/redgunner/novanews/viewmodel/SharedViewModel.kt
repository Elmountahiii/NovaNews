package com.redgunner.novanews.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.redgunner.novanews.models.category.Categories
import com.redgunner.novanews.models.comment.Comments
import com.redgunner.novanews.models.post.Post
import com.redgunner.novanews.repository.WordPressRepository
import com.redgunner.novanews.state.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val wordPressRepository: WordPressRepository) :
    ViewModel() {


    val savedPosts = wordPressRepository.savedPosts.asLiveData()

    var tabLayoutPosition = 0
    private var localPostId = 0




    private val currentCategoryPosition = MutableLiveData(DEFAULT_CATEGORY_POSITION)


    val posts = currentCategoryPosition.switchMap { categoryId ->

        if (categories.value.isNotEmpty()) {
            wordPressRepository.getPostByCategory(categories.value[categoryId].id)
                .cachedIn(viewModelScope)

        } else {
            wordPressRepository.getPostByCategory(categoryId).cachedIn(viewModelScope)

        }

    }


    private val _categoryList = MutableStateFlow<List<Categories>>(emptyList())
    val categories: StateFlow<List<Categories>> = _categoryList


    private val _postState = MutableStateFlow<PostState>(PostState.Empty)
    val postState: StateFlow<PostState> = _postState

    private val _commentEventChannel = Channel<List<Comments>>()
    val comments = _commentEventChannel.receiveAsFlow()


    private val _searchEventChannel = Channel<List<Post>>()
    val searchList = _searchEventChannel.receiveAsFlow()



    init {

        viewModelScope.launch {

            try {
                _categoryList.value = wordPressRepository.getCategories()

            } catch (exception: IOException) {

            } catch (exception: HttpException) {

            }


        }
    }

    fun getPostById(postId: Int) {
        if (postId != localPostId) {
            viewModelScope.launch {
                _postState.value = PostState.Loading


                try {


                    _postState.value =
                        PostState.Success(wordPressRepository.getPostById(postId = postId))
                    localPostId = postId

                } catch (exception: IOException) {

                    _postState.value = PostState.Error(exception.message.toString())
                } catch (exception: HttpException) {
                    _postState.value = PostState.Error(exception.message.toString())

                }

            }

        }

    }

    fun getPostByCategory(categoryPosition: Int) {


        currentCategoryPosition.value = categoryPosition


    }

    fun getPostComments(postId: Int) {

        viewModelScope.launch {

            try {


                _commentEventChannel.send(wordPressRepository.getPostComments(postId))
            } catch (e: Exception) {


            }
        }


    }

    fun search(query: String) {

        viewModelScope.launch {
            try {

                _searchEventChannel.send(wordPressRepository.searchPost(query))

            } catch (e: Exception) {


            }
        }

    }

    fun savePost(post: Post) {

        viewModelScope.launch {

            try {


               wordPressRepository.addPostToDatabase(post)


            } catch (exception: IOException) {


            } catch (exception: HttpException) {

            }

        }
    }

    fun saveTabLayoutPosition(position: Int) {
        tabLayoutPosition = position
    }

    companion object {
        private const val DEFAULT_CATEGORY_POSITION = 0
    }


}