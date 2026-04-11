package com.example.islamiapp.ui.screens.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.islamiapp.R
import com.example.islamiapp.ui.screens.main.fragments.hadeth.HadethFragment
import com.example.islamiapp.ui.screens.main.fragments.quran.QuranFragment
import com.example.islamiapp.ui.screens.main.fragments.sebha.SebhaFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, QuranFragment())
            .commit()
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.quranTab -> {
                    showFragment(QuranFragment())
                }

                R.id.hadethTab -> {
                    showFragment(HadethFragment())
                }

                R.id.sebhaTab -> {
                    showFragment(SebhaFragment())
                }

                R.id.radioTab -> {
                    Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}