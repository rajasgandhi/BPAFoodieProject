package com.rmgstudios.bpafoodie

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class FeedPostsAdapter(posts: List<FeedData>) :
    RecyclerView.Adapter<FeedPostsAdapter.ViewHolder>() {

    // no Context reference needed—can get it from a ViewGroup parameter
    private var posts = ArrayList<FeedData>(posts)

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // no need for a LayoutInflater instance—
        // the custom view inflates itself
        val itemView = FeedPost(parent.context)
        // manually set the CustomView's size
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCustomView().setUpFeedPost(posts[position].postTitle, posts[position].postBody)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val customView: FeedPost = v as FeedPost
        fun getCustomView(): FeedPost {
            return customView
        }
    }
}