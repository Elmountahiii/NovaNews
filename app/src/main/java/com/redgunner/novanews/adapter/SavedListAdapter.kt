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
import com.redgunner.novanews.models.Local.LocalPost
import com.redgunner.novanews.models.post.Post
import com.redgunner.novanews.state.PostClickState
import kotlinx.android.synthetic.main.saved_post_view_holder.view.*
import java.text.SimpleDateFormat

class SavedListAdapter(val postClick: (PostClickState) -> Unit):
    ListAdapter<Post, SavedListAdapter.SavedViewHolder>(SavedComparator())
{


    inner class SavedViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {


        private val image = itemView.SavedPostImage
        private val title = itemView.SavedPostTitle
        private val time = itemView.savedPostTimeAndCategory


        init {


            image.setOnClickListener {
                getItem(adapterPosition)?.let { post -> postClick(PostClickState.NormalClick(post.id)) }
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




            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            time.text = formatter.format(parser.parse(post.date))


        }


    }


    class SavedComparator() : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return true
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_post_view_holder, parent, false)
        return SavedViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

}