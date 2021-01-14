package com.rmgstudios.hapori

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rmgstudios.hapori.helpers.FeedPost
import com.rmgstudios.hapori.helpers.RecipeListData

class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_expanded)

        val feedPost = findViewById<FeedPost>(R.id.post_expanded1)
        //feedPost.setUpFeedPost("RG")
        val sendComment = findViewById<ImageView>(R.id.send_comment)
        val commentInput = findViewById<EditText>(R.id.comment_input)
        val postTitle: String? =
            intent.extras?.getSerializable("POST_TITLE") as String?
        val postBody: String? =
            intent.extras?.getSerializable("POST_BODY") as String?
        feedPost.setUpFeedPost(postTitle!!, postBody!!)
        sendComment.setOnClickListener {

            val ref = FirebaseDatabase.getInstance().reference
            try {
                val commentDetails =
                    "{\"comment\":\"" + commentInput.text.trimEnd() + "\"}"
                val jsonMap: Map<String, Any> = Gson().fromJson(
                    commentDetails,
                    object : TypeToken<HashMap<String?, Any?>?>() {}.type
                )
                //ref.child("comments").push().setValue(commentInput.text.trimEnd().toString())
                ref.child("comments").push().setValue(jsonMap)
                Log.d("TAG","suceess")
            } catch (e : Exception) {
                Log.d("TAG", e.toString())
            }
        }
    }

}