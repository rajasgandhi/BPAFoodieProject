package com.rmgstudios.hapori.helpers

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rmgstudios.hapori.R
import com.rmgstudios.hapori.adapters.CommentAdapter

class PostExpanded : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_expanded)

        val backArrow = findViewById<ImageView>(R.id.post_exit_arrow)

        backArrow.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }

        val postTitle: String? =
            intent.extras?.getSerializable("POST_TITLE") as String?
        val postBody: String? =
            intent.extras?.getSerializable("POST_BODY") as String?
        val postID: String? =
            intent.extras?.getSerializable("POST_ID") as String?

        val feedPost = findViewById<FeedPost>(R.id.post_expanded1)
        val sendComment = findViewById<ImageView>(R.id.send_comment)
        val commentInput = findViewById<EditText>(R.id.comment_input)
        val commentFeedListView = findViewById<RecyclerView>(R.id.feed_comments)

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
                ref.child("comments/$postID").setValue(jsonMap)
                retrieveComments(postID!!, commentFeedListView)
                commentInput.text.clear()
            } catch (e: Exception) {
                Log.d("TAG", e.toString())
            }
        }
    }

    private fun retrieveComments(postID: String, feedComments: RecyclerView) {
        val ref = FirebaseDatabase.getInstance().reference.child("comments/$postID")
        val commentList = ArrayList<CommentData>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()
                for (messageSnapshot in dataSnapshot.children) {
                    val comment = messageSnapshot.value as String
                    commentList.add(
                        0,
                        CommentData(
                            comment
                        )
                    )
                    val listAdapter = CommentAdapter(this@PostExpanded, commentList)

                    feedComments.adapter = listAdapter
                    feedComments.layoutManager = LinearLayoutManager(this@PostExpanded)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@PostExpanded, "Error! Please try again.", Toast.LENGTH_SHORT)
                    .show()
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        ref.addListenerForSingleValueEvent(listener)
    }

}