package com.rmgstudios.hapori

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rmgstudios.hapori.helpers.RecipeListAdapter
import com.rmgstudios.hapori.helpers.RecipeListData
import com.rmgstudios.hapori.helpers.SpacesItemDecoration


class RecipeResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_results)

        val recipeListView = findViewById<RecyclerView>(R.id.recipeList)

        val returnedRecipeList: ArrayList<RecipeListData>? =
            intent.extras?.getSerializable("RECIPE_LIST") as ArrayList<RecipeListData>?

        val listAdapter =
            RecipeListAdapter(returnedRecipeList!!, this)
        recipeListView.addItemDecoration(
            SpacesItemDecoration(20, ContextCompat.getDrawable(this, R.drawable.list_divider)!!)
        )
        recipeListView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )
        recipeListView.adapter = listAdapter
        recipeListView.layoutManager = LinearLayoutManager(this)
    }
}