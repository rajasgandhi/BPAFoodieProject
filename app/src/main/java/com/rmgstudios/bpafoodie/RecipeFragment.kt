package com.rmgstudios.bpafoodie

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class RecipeFragment : Fragment() {
    fun RecipeFragment() {
        // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_recipe, container, false)
        val getRecipesBtn = view.findViewById<Button>(R.id.getRecipesBtn)
        val recipeListView = view.findViewById<RecyclerView>(R.id.recipeList)
        val ingredientInput = view.findViewById<EditText>(R.id.ingredientInput)
        val recipeList1 = ArrayList<RecipeListData>()

        recipeListView.adapter = RecipeListAdapter(recipeList1)
        recipeListView.layoutManager = LinearLayoutManager(activity!!)

        getRecipesBtn.setOnClickListener {

            if (ingredientInput.text.isNullOrBlank()) {
                Toast.makeText(
                    activity!!,
                    "Please make sure your ingredients are not blank!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://api.spoonacular.com/recipes/complexSearch?apiKey=" + getString(R.string.spoonacularApiKey) + "&diet=vegetarian&includeIngredients=" + ingredientInput.text)
                    .get()
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")

                            recipeList1.clear()

                            val responseBody = response.body!!.string()

                            val jsonObject = JSONObject(responseBody)
                            val results = jsonObject.getJSONArray("results")
                            val resultLength = results.length()

                            for (i in 0 until resultLength) {
                                recipeList1.add(
                                    RecipeListData(
                                        (results[i] as JSONObject).getString("title").toString(),
                                        (results[i] as JSONObject).getString("image").toString()
                                    )
                                )
                            }
                            if (recipeList1.isEmpty()) {
                                activity!!.runOnUiThread {
                                    Toast.makeText(
                                        activity!!,
                                        "Please make sure you have vegetarian ingredients!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                activity!!.runOnUiThread {
                                    val intent = Intent(activity!!, RecipeResults::class.java)
                                    intent.putExtra("RECIPE_LIST", recipeList1)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                })
            }
        }
        changeTitleTextSize(view.findViewById(R.id.recipeGeneratorText))
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
}