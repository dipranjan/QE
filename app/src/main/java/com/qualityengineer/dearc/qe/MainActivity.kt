package com.qualityengineer.dearc.qe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.content.Intent


class MainActivity : AppCompatActivity() {


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Making notification bar transparent
                val window = window
                if (window != null) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                }
            setContentView(R.layout.activity_main)
            setSupportActionBar(findViewById(R.id.my_toolbar))



    }
    fun navigateLR(view: View) {
        val intent = Intent(this, LearningResource::class.java).apply{}
        startActivity(intent)
    }
}
