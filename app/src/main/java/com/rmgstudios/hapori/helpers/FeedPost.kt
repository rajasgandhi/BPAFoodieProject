package com.rmgstudios.hapori.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View

import android.widget.RelativeLayout
import android.widget.TextView
import com.rmgstudios.hapori.R


class FeedPost(context: Context?) : RelativeLayout(context) {
    private var mInflater: LayoutInflater = LayoutInflater.from(context)

    fun setUpFeedPost(postTitleText: String = "Title", postBodyText: String = "Body") {
        val v: View = mInflater.inflate(R.layout.post_layout, this, true)
        val postTitle = v.findViewById(R.id.post_title) as TextView
        val postBody = v.findViewById(R.id.post_body) as TextView
        postTitle.text = postTitleText
        postBody.text = postBodyText
    }
}