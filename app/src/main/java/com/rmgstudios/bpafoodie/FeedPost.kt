package com.rmgstudios.bpafoodie

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import android.widget.RelativeLayout
import android.widget.TextView


class FeedPost : RelativeLayout {
    private var mInflater: LayoutInflater
    private var postTitleText: String
    private var postBodyText: String

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        postTitleText = "This is the Title"
        postBodyText = "This is the Body"
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mInflater = LayoutInflater.from(context)
        postTitleText = ""
        postBodyText = ""
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.FeedPost, 0, 0)
            try {
                this.postTitleText =
                    attributes.getString(R.styleable.FeedPost_postTitleText).toString()
                this.postBodyText =
                    attributes.getString(R.styleable.FeedPost_postBodyText).toString()
            } finally {
                attributes.recycle()
            }
        } else {
            throw NullPointerException("AttributeSet is null!")
        }
    }

    fun setUpFeedPost(postTitleText: String = "Title", postBodyText: String = "Body") {
        val v: View = mInflater.inflate(R.layout.post_layout, this, true)
        val postTitle = v.findViewById(R.id.post_title) as TextView
        val postBody = v.findViewById(R.id.post_body) as TextView
        postTitle.text = postTitleText
        postBody.text = postBodyText
    }
}