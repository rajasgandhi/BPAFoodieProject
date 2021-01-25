package com.rmgstudios.hapori.helpers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rmgstudios.hapori.MainActivity
import com.rmgstudios.hapori.R
import com.rmgstudios.hapori.adapters.CommentAdapter
import com.rmgstudios.hapori.fragments.HomeFragment


class PostExpanded : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_expanded)

        val backArrow = findViewById<ImageView>(R.id.post_exit_arrow)

        backArrow.setOnClickListener {
            finish()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

        val postTitle: String? =
            intent.extras?.getSerializable("POST_TITLE") as String?
        val postBody: String? =
            intent.extras?.getSerializable("POST_BODY") as String?
        val postID: String? =
            intent.extras?.getSerializable("POST_ID") as String?

        val postTitleComment = findViewById<TextView>(R.id.post_title_comment)
        val postBodyComment = findViewById<TextView>(R.id.post_body_comment)
        val sendComment = findViewById<ImageView>(R.id.send_comment)
        val commentInput = findViewById<EditText>(R.id.comment_input)
        val commentFeedListView = findViewById<RecyclerView>(R.id.feed_comments)

        retrieveComments(postID!!, commentFeedListView)

        postTitleComment.text = postTitle
        postBodyComment.text = postBody

        sendComment.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().reference
            try {
                val commentDetails =
                    "{\"comment\":\"" + commentInput.text.trimEnd() + "\"}"
                val jsonMap: Map<String, Any> = Gson().fromJson(
                    commentDetails,
                    object : TypeToken<HashMap<String?, Any?>?>() {}.type
                )
                ref.child("comments/$postID").push().setValue(jsonMap)
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
                    ref.child(messageSnapshot.key!!).child("comment")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                try {
                                    if (snapshot.value != null) {
                                        val comment = snapshot.value.toString()
                                        commentList.add(
                                            0,
                                            CommentData(comment)
                                        )
                                    } else {
                                        Log.e("TAG", " it's null.")
                                    }
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }
                                val listAdapter = CommentAdapter(this@PostExpanded, commentList)

                                feedComments.adapter = listAdapter

                                feedComments.requestLayout()
                                feedComments.layoutManager = LinearLayoutManager(this@PostExpanded)
                                ref.removeEventListener(this)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("TAG", error.message)
                                Toast.makeText(
                                    this@PostExpanded,
                                    "Oops! An error occurred, please try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ref.removeEventListener(this)
                            }
                        })
                }
                ref.removeEventListener(this)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@PostExpanded, "Error! Please try again.", Toast.LENGTH_SHORT)
                    .show()
                println("loadPost:onCancelled ${databaseError.toException()}")
                ref.removeEventListener(this)
            }
        }
        ref.addListenerForSingleValueEvent(listener)
    }

}