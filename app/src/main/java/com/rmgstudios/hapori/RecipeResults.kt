package com.rmgstudios.hapori

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_results)

        val recipeListView = findViewById<RecyclerView>(R.id.recipeList)

        val returnedRecipeList: ArrayList<RecipeListData>? =
            intent.extras?.getSerializable("RECIPE_LIST") as ArrayList<RecipeListData>?

        val listAdapter =
            RecipeListAdapter(returnedRecipeList!!)
        recipeListView.addItemDecoration(
            DividerItemDecoration(
                recipeListView.context,
                DividerItemDecoration.HORIZONTAL
            )
        )
        recipeListView.adapter = listAdapter
        recipeListView.layoutManager = LinearLayoutManager(this)
        /*(recipeListView.layoutManager as LinearLayoutManager).isMeasurementCacheEnabled = true

        */
    }
}