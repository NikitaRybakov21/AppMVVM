package com.example.appmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmvvm.R
import com.example.mylibrary.TestModule
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

   /*   val splash = instalSplashScreen()
        splash.setKeepOnScrenCondition  { true }

        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(3000)
            splash.setKeepOnScrenCondition { false }
        }  */

        setContentView(R.layout.activity_main)
        setFirstFragment()

        TestModule.test("test string")
    }

    private fun setFirstFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,MainFragment.newInstance())
            .addToBackStack("Stack")
            .commit()
    }
}