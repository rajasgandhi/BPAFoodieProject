package com.rmgstudios.bpafoodie

import android.content.Intent
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var mSwipeRefreshLayout : SwipeRefreshLayout
    private lateinit var feedListView : RecyclerView
    private lateinit var addPostFab : FloatingActionButton
    private var feedList = ArrayList<FeedData>()
    fun HomeFragment() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        changeTitleTextSize(view.findViewById(R.id.feedText))

        val bottomSheetView = view.findViewById<BottomSheetLayout>(R.id.bottomSheet)

        feedListView = view.findViewById(R.id.feedPosts)
        addPostFab = view.findViewById(R.id.addPostFab)

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        feedListView.adapter = FeedPostsAdapter(activity!!, feedList)
        feedListView.layoutManager = LinearLayoutManager(activity!!)

        retrieveFeed()

        mSwipeRefreshLayout.setOnRefreshListener{
            mSwipeRefreshLayout.isRefreshing = true
            retrieveFeed()
        }

        addPostFab.setOnClickListener {
            val postView = LayoutInflater.from(activity!!).inflate(R.layout.main_post_creation, bottomSheetView, false)
            val closeButton = postView.findViewById<ImageView>(R.id.exit_create_post)
            val postTitle = postView.findViewById<EditText>(R.id.title_edit_text)
            val postDescription = postView.findViewById<EditText>(R.id.description_edit_text)
            val postBtn = postView.findViewById<Button>(R.id.post_button)
            if(postTitle.text.toString() == "" || postDescription.text.toString() == "") {
                postBtn.alpha = 0.3f
            }
            postTitle.setOnKeyListener(View.OnKeyListener { _, _, _ ->
                if(postTitle.text.toString() == "" || postDescription.text.toString() == "") {
                    postBtn.alpha = 0.3f
                } else {
                    postBtn.alpha = 1f
                }
                return@OnKeyListener false
            })
            postDescription.setOnKeyListener(View.OnKeyListener { _, _, _ ->
                if(postTitle.text.toString() == "" || postDescription.text.toString() == "") {
                    postBtn.alpha = 0.3f
                } else {
                    postBtn.alpha = 1f
                }
                return@OnKeyListener false
            })
            closeButton.setOnClickListener{
                if(bottomSheetView.isSheetShowing)
                    bottomSheetView.dismissSheet()
            }
            postBtn.setOnClickListener{
                if(postBtn.alpha != 0.3f) {
                    val client = OkHttpClient()

                    val json = "{\"title\":\"" + postTitle.text.toString() + "\",\"body\":\"" + postDescription.text.toString() + "\"}"

                    val body = json.toRequestBody("application/json".toMediaTypeOrNull())

                    val request = Request.Builder()
                        .url(getString(R.string.url_send_post))
                        .post(body)
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.use {
                                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                                val responseBody = response.body!!.string()

                                Log.d("TAG", responseBody)
                            }
                        }
                    })
                    bottomSheetView.dismissSheet()
                }
            }

            val displayMetrics = DisplayMetrics()

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                activity!!.display!!.getRealMetrics(displayMetrics)
            } else activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)

            val height = displayMetrics.heightPixels

            bottomSheetView.peekSheetTranslation = height.toFloat()
            bottomSheetView.showWithSheetView(postView)
        }

        return view
    }

    private fun changeTitleTextSize(title: TextView) {
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            activity!!.display!!.getRealMetrics(displayMetrics)
        } else activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        title.textSize = (.037037 * width).toFloat()
    }

    private fun retrieveFeed() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(getString(R.string.url_get_posts))
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body!!.string()

                    val jsonArray = JSONArray(responseBody)
                    val resultLength = jsonArray.length()

                    val listAdapter =
                        FeedPostsAdapter(activity!!, feedList)

                    feedList.clear()

                    for (i in 0 until resultLength) {
                        feedList.add(
                            FeedData(
                                (jsonArray[i] as JSONObject).getString("title").toString(),
                                (jsonArray[i] as JSONObject).getString("body").toString()
                            )
                        )
                    }
                    listAdapter.notifyDataSetChanged()
                    activity!!.runOnUiThread {
                        feedListView.adapter = listAdapter
                        feedListView.layoutManager = LinearLayoutManager(activity!!)
                    }
                }
            }
        })
        mSwipeRefreshLayout.isRefreshing = false
    }
}