package com.rmgstudios.hapori.fragments

import android.content.Context
import android.os.Bundle
import android.os.Looper
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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rmgstudios.hapori.R
import com.rmgstudios.hapori.helpers.FeedData
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import com.rmgstudios.hapori.helpers.FeedPostsAdapter


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
            Log.d("TAG", "got here")
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

            /*postTitle.onFocusChangeListener = View.OnFocusChangeListener { view: View, b: Boolean ->
                if(!b) {
                    hideKeyboard(view)
                }
            }
            postDescription.onFocusChangeListener = View.OnFocusChangeListener { view: View, b: Boolean ->
                if(!b) {
                    hideKeyboard(requireActivity())
                }
            }*/
            closeButton.setOnClickListener {
                if (bottomSheetView.isSheetShowing)
                    bottomSheetView.dismissSheet()
            }
            postBtn.setOnClickListener {
                val db = Firebase.database
                val ref = db.getReference("/post")
                ref.setValue("Hello, world")
                retrieveFeed()
                /*if (postBtn.alpha != 0.3f) {
                    val client = OkHttpClient()

                    val json =
                        "{\"title\":\"" + postTitle.text.toString() + "\",\"body\":\"" + postDescription.text.toString() + "\"}"

                    val body = json.toRequestBody("application/json".toMediaTypeOrNull())

                    val request: Request =
                        Request.Builder()
                            .url(getString(R.string.url_send_post))
                            .post(body)
                            .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Looper.prepare()
                            Toast.makeText(
                                requireActivity(),
                                "Oops! An error occurred, please try again later!",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.use {
                                if (!response.isSuccessful) {
                                    Looper.prepare()
                                    Toast.makeText(
                                        requireActivity(),
                                        "Oops! An error occurred, please try again!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    throw IOException("Unexpected code $response")
                                }

                                val responseBody = response.body!!.string()

                                Log.d("TAG", responseBody)
                            }
                        }
                    })
                    bottomSheetView.dismissSheet()
                }*/
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
        val client = OkHttpClient()

        val request: Request = Request.Builder()
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
                        FeedPostsAdapter(requireActivity(), feedList)

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
                    requireActivity().runOnUiThread {
                        feedListView.adapter = listAdapter
                        feedListView.layoutManager = LinearLayoutManager(requireActivity())
                    }
                }
            }
        })
    }

    fun convertPixelsToDp(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}