package com.rmgstudios.bpafoodie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val fragment1: Fragment = HomeFragment()
    private val fragment2: Fragment = RecipeFragment()
    private val fragment3: Fragment = WaterReminderFragment()
    private val fragment4: Fragment = AboutFragment()
    private val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragment1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavBar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    //Log.d("TAG", "hoem clicked")
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                }
                R.id.nav_recipes -> {
                    //Log.d("TAG", "searcg clicked")
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                }
                R.id.nav_water_reminder -> {
                    //Log.d("TAG", "settings clicked")
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                }
                R.id.nav_about -> {
                    //Log.d("TAG", "about clicked")
                    fm.beginTransaction().hide(active).show(fragment4).commit()
                    active = fragment4
                }

            }
            true
        }
        fm.beginTransaction().add(R.id.main_container, fragment4, "4").hide(fragment4).commit()
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit()
    }
}