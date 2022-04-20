package com.example.appmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmvvm.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFirstFragment()
    }

    private fun setFirstFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,MainFragment.newInstance())
            .addToBackStack("Stack")
            .commit()
    }
}