package com.rmgstudios.hapori.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.rmgstudios.hapori.R
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class RecipeListAdapter(
    private val recipes: ArrayList<RecipeListData>,
    private val context: Context
) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImg)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val listRowView = inflater.inflate(R.layout.list_row, parent, false)
        // Return a new holder instance
        return ViewHolder(listRowView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val recipe: RecipeListData = recipes[position]
        // Set item views based on your views and data model
        val textView = viewHolder.recipeName
        val image = viewHolder.recipeImage

        textView.text = recipe.recipeName
        if (recipe.recipeImgUrl != "") {
            Picasso.get()
                .load(
                    (recipe.recipeImgUrl)
                ).into(image)
        }
        viewHolder.itemView.setOnClickListener {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.spoonacular.com/recipes/" + recipe.recipeId + "/information?apiKey=" + context.resources.getString(R.string.spoonacularApiKey) + "&includeNutrition=false")
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

                        val jsonObject = JSONObject(responseBody)
                        val recipeURL = jsonObject.getString("sourceUrl")
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(recipeURL)
                        context.startActivity(i)
                    }
                }
            })
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return recipes.size
    }
}

