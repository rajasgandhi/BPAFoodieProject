package com.rmgstudios.hapori.helpers

import android.content.Context

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import android.widget.RelativeLayout
import android.widget.TextView
import com.rmgstudios.hapori.R


class PostComment : RelativeLayout {
    private var mInflater: LayoutInflater

    //private var mInflater: LayoutInflater = LayoutInflater.from(context)
    var commentBody: String

    constructor(context: Context?) : super(context) {
        mInflater = LayoutInflater.from(context)
        commentBody = ""
    }

    /*constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
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
    }*/

    fun setUpComment(commentBodyText: String = "Body") {
        val v: View = mInflater.inflate(R.layout.comment_layout, this, true)
        val commentBody = v.findViewById(R.id.comment_body) as TextView
        commentBody.text = commentBodyText
    }
}