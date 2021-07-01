package com.redgunner.novanews.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redgunner.novanews.R
import com.redgunner.novanews.models.post.Post
import com.redgunner.novanews.state.PostClickState
import kotlinx.android.synthetic.main.post_view_holder_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostListAdapter(val postClick: (PostClickState) -> Unit) :
    PagingDataAdapter<Post, PostListAdapter.PostViewHolder>(POST_COMPARATOR) {


    private val listOfSaved = mutableListOf<String>()

    inner class PostViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {


        private val image = itemView.PostImage
        private val title = itemView.PostTitle
        private val category = itemView.postCategory
        private val time = itemView.PostTime
        private val saved = itemView.SavePost

        init {


            image.setOnClickListener { view ->
                getItem(absoluteAdapterPosition)?.let { post -> postClick(PostClickState.NormalClick(post.id)) }
            }


            saved.setOnCheckedChangeListener { buttonView, isChecked ->


                getItem(absoluteAdapterPosition)?.let { post ->

                    if (isChecked) {

                        listOfSaved.add(post.title.rendered)

                        postClick(PostClickState.SavedClick(post))
                    }

                }


            }


        }


        fun bind(post: Post) {


            saved.isChecked = listOfSaved.contains(post.title.rendered)


            if (!post._embedded.wp_FeaturedMedia.isNullOrEmpty()) {

                Glide.with(context)
                    .load(post._embedded.wp_FeaturedMedia[0].source_url)
                    .placeholder(ColorDrawable(Color.parseColor("#e2e2e2")))
                    .into(image)

            }



            title.text = HtmlCompat.fromHtml(post.title.rendered, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()


            category.text = post._embedded.wp_Term[0][0].name


            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("dd.MM.yyyy",Locale.getDefault())
            time.text = formatter.format(parser.parse(post.date)!!)


        }


    }


    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_view_holder_layout, parent, false)
        return PostViewHolder(view, parent.context)
    }


}