package com.rmgstudios.hapori.adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.rmgstudios.hapori.helpers.CommentData
import com.rmgstudios.hapori.helpers.PostComment


class CommentAdapter(private var context: Context, comments: List<CommentData>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    // no Context reference needed—can get it from a ViewGroup parameter
    private var comments = ArrayList<CommentData>(comments)

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // no need for a LayoutInflater instance—
        // the custom view inflates itself
        val itemView = PostComment(parent.context)

        return ViewHolder(itemView)
    }

    private fun dpToPx(dp: Float) : Int {
        val r: Resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCustomView().setUpComment(comments[position].commentBody)
        if (position == comments.size-1) {

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(holder.getCustomView().marginLeft, holder.getCustomView().marginTop, holder.getCustomView().marginRight, dpToPx(16f))
            holder.getCustomView().layoutParams = params
            Log.d("TAG", holder.getCustomView().marginBottom.toString())
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val customView: PostComment = v as PostComment
        fun getCustomView(): PostComment {
            return customView
        }
    }
}