package com.redgunner.novanews.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redgunner.novanews.R
import com.redgunner.novanews.models.post.Post
import com.redgunner.novanews.state.PostClickState
import kotlinx.android.synthetic.main.post_view_holder_layout.view.*
import java.text.SimpleDateFormat

class SearchListAdapter(val postClick: (PostClickState) -> Unit) :
    ListAdapter<Post, SearchListAdapter.SearchViewHolder>(SearchComparator()) {


    inner class SearchViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        private val image = itemView.PostImage
        private val title = itemView.PostTitle
        private val category = itemView.postCategory
        private val time = itemView.PostTime
        private val saved=itemView.SavePost

        init {

            image.setOnClickListener {view ->
                getItem(adapterPosition)?.let { post -> postClick(PostClickState.NormalClick(post.id)) }
            }


            saved.setOnClickListener {
                getItem(adapterPosition)?.let { post -> postClick(PostClickState.SavedClick(post)) }

            }




        }


        fun bind(post: Post) {


            if (!post._embedded.wp_FeaturedMedia.isNullOrEmpty()) {

                Glide.with(context)
                    .load(post._embedded.wp_FeaturedMedia[0].source_url)
                    .placeholder(ColorDrawable(Color.parseColor("#e2e2e2")))
                    .into(image)

            }

            title.text = Html.fromHtml(Html.fromHtml(post.title.rendered).toString())


            category.text = post._embedded.wp_Term[0][0].name


            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            time.text = formatter.format(parser.parse(post.date))


        }


    }


    class SearchComparator() : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return true
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_view_holder_layout, parent, false)
        return SearchViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val current = getItem(position)
        holder.bind(current)
    }


}