package com.rmgstudios.hapori

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
class RecipeListAdapter(private val recipes: ArrayList<RecipeListData>) :
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
    ): RecipeListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.list_row, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: RecipeListAdapter.ViewHolder, position: Int) {
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

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return recipes.size
    }
}

