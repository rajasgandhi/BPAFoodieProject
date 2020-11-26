package com.rmgstudios.bpafoodie

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

        feedListView = view.findViewById(R.id.feedPosts)
        addPostFab = view.findViewById(R.id.addPostFab)

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        feedListView.adapter = FeedPostsAdapter(feedList)
        feedListView.layoutManager = LinearLayoutManager(activity!!)

        retrieveFeed()

        mSwipeRefreshLayout.setOnRefreshListener{
            mSwipeRefreshLayout.isRefreshing = true
            retrieveFeed()
        }

        addPostFab.setOnClickListener {
            val client = OkHttpClient()

            val json = "{\"title\":\"this is the title\",\"body\":\"this is the body\"}"

            val body = json.toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("http://10.0.2.2:3000/sendpost")
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
            .url("http://10.0.2.2:3000/getposts")
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
                        FeedPostsAdapter(feedList)

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