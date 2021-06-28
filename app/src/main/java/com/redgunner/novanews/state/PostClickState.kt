package com.redgunner.novanews.state

import com.redgunner.novanews.models.post.Post

sealed class PostClickState
{
    data class NormalClick(val PostId:Int):PostClickState()
    data class SavedClick(val post:Post):PostClickState()

}
