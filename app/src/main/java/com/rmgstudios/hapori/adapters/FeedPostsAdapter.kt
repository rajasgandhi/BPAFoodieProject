package com.rmgstudios.hapori.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rmgstudios.hapori.R
import com.rmgstudios.hapori.helpers.FeedData
import com.rmgstudios.hapori.helpers.FeedPost
import com.rmgstudios.hapori.helpers.PostExpanded
import kotlin.collections.ArrayList


class FeedPostsAdapter(
    private var context: Context,
    posts: List<FeedData>,
    val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<FeedPostsAdapter.ViewHolder>() {

    // no Context reference needed—can get it from a ViewGroup parameter
    private var posts = ArrayList<FeedData>(posts)

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // no need for a LayoutInflater instance—
        // the custom view inflates itself
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)
        return ViewHolder(itemView)
    }

    private fun dpToPx(dp: Float): Int {
        val r: Resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post, itemClickListener)
        //holder.getCustomView().setUpFeedPost(post.postTitle, post.postBody)
        /*holder.getCustomView().setOnClickListener {
            val i = Intent(context, PostExpanded::class.java)
            i.putExtra("POST_TITLE", posts[position].postTitle)
            i.putExtra("POST_BODY", posts[position].postBody)
            i.putExtra("POST_ID", posts[position].postID)
            context.startActivity(i)
            //((context) as Activity).overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }*/
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        /*private val customView: FeedPost = v as FeedPost
        fun getCustomView(): FeedPost {
            return customView
        }*/
        val title = v.findViewById<TextView>(R.id.post_title)
        val body = v.findViewById<TextView>(R.id.post_body)
        fun bind(data: FeedData, clickListener: OnItemClickListener) {
            title.text = data.postTitle
            body.text = data.postBody

            itemView.setOnClickListener {
                clickListener.onItemClicked(data)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(data: FeedData)
    }
}