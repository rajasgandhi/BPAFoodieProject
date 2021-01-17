package com.rmgstudios.hapori.fragments

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.flipboard.bottomsheet.BottomSheetLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rmgstudios.hapori.R
import com.rmgstudios.hapori.helpers.FeedData
import com.rmgstudios.hapori.adapters.FeedPostsAdapter
import okhttp3.*


class HomeFragment : Fragment() {
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var feedListView: RecyclerView
    private lateinit var addPostFab: FloatingActionButton
    private var feedList = ArrayList<FeedData>()
    fun HomeFragment() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseApp.initializeApp(requireActivity())
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        changeTitleTextSize(view.findViewById(R.id.feedText))

        val bottomSheetView = view.findViewById<BottomSheetLayout>(R.id.bottomSheet)

        feedListView = view.findViewById(R.id.feedPosts)
        addPostFab = view.findViewById(R.id.addPostFab)

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        feedListView.adapter = FeedPostsAdapter(requireActivity(), feedList)
        feedListView.layoutManager = LinearLayoutManager(requireActivity())

        retrieveFeed()

        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.isRefreshing = true
            retrieveFeed()
            mSwipeRefreshLayout.isRefreshing = false
        }

        addPostFab.setOnClickListener {
            val postView = LayoutInflater.from(requireActivity()).inflate(
                R.layout.main_post_creation,
                bottomSheetView,
                false
            )
            val closeButton = postView.findViewById<ImageView>(R.id.exit_create_post)
            val postTitle = postView.findViewById<EditText>(R.id.title_edit_text)
            val postDescription = postView.findViewById<EditText>(R.id.description_edit_text)
            val postBtn = postView.findViewById<Button>(R.id.post_button)
            if (postTitle.text.toString() == "" || postDescription.text.toString() == "") {
                postBtn.alpha = 0.3f
            }

            val displayMetrics = DisplayMetrics()

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                requireActivity().display!!.getRealMetrics(displayMetrics)
            } else requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            postTitle.width = (width - convertPixelsToDp(32f, requireActivity())).toInt()
            postDescription.width = (width - convertPixelsToDp(32f, requireActivity())).toInt()

            postTitle.setOnKeyListener(View.OnKeyListener { _, _, _ ->
                if (postTitle.text.toString() == "" || postDescription.text.toString() == "") {
                    postBtn.alpha = 0.3f
                } else {
                    postBtn.alpha = 1f
                }
                return@OnKeyListener false
            })

            postDescription.setOnKeyListener(View.OnKeyListener { _, _, _ ->
                if (postTitle.text.toString() == "" || postDescription.text.toString() == "") {
                    postBtn.alpha = 0.3f
                } else {
                    postBtn.alpha = 1f
                }
                return@OnKeyListener false
            })

            closeButton.setOnClickListener {
                if (bottomSheetView.isSheetShowing)
                    bottomSheetView.dismissSheet()
            }

            postBtn.setOnClickListener {
                if (postTitle.text.toString() != "" && postDescription.text.toString() != "") {
                    val ref = FirebaseDatabase.getInstance().reference
                    try {
                        val postDetails =
                            "{\"title\":\"" + postTitle.text.toString() + "\",\"body\":\"" + postDescription.text.toString() + "\"}"
                        val jsonMap: Map<String, Any> = Gson().fromJson(
                            postDetails,
                            object : TypeToken<HashMap<String?, Any?>?>() {}.type
                        )
                        ref.child("posts").push().setValue(jsonMap)
                    } catch (e : Exception) {
                        Log.d("TAG", e.toString())
                    }
                    bottomSheetView.dismissSheet()
                    retrieveFeed()
                }
            }

            bottomSheetView.peekSheetTranslation = height.toFloat()
            bottomSheetView.showWithSheetView(postView)
        }

        return view
    }

    private fun changeTitleTextSize(title: TextView) {
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            requireActivity().display!!.getRealMetrics(displayMetrics)
        } else requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        title.textSize = (.037037 * width).toFloat()
    }

    private fun retrieveFeed() {
        val ref = FirebaseDatabase.getInstance().reference.child("posts")
        //val ref1 = FirebaseDatabase.getInstance().reference.child("comments")
        //ref1.push().setValue("hi")
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                feedList.clear()
                for (messageSnapshot in dataSnapshot.children) {
                    val postName = messageSnapshot.child("title").value as String
                    val postBody = messageSnapshot.child("body").value as String
                    val postID = messageSnapshot.key!!
                    feedList.add(
                        0,
                        FeedData(
                            postName,
                            postBody,
                            postID
                        )
                    )
                    val listAdapter =
                        FeedPostsAdapter(requireActivity(), feedList)
                    requireActivity().runOnUiThread {
                        feedListView.adapter = listAdapter
                        feedListView.layoutManager = LinearLayoutManager(requireActivity())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireActivity(), "Error! Please try again.", Toast.LENGTH_SHORT)
                    .show()
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        ref.addListenerForSingleValueEvent(listener)
    }

    private fun convertPixelsToDp(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}