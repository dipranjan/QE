package com.qualityengineer.dearc.qe

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager

class LearningResource : AppCompatActivity() {


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Making notification bar transparent
                val window = window
                if (window != null) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                }
            setContentView(R.layout.learning_resource)
            setSupportActionBar(findViewById(R.id.my_toolbar))



    }

    fun navigateJava(view: View) {
        val intent = Intent(this, Content::class.java).apply{}
        intent.putExtra("catagory", "JAVA")
        startActivity(intent)
    }
    fun navigateMaven(view: View) {
        val intent = Intent(this, Content::class.java).apply{}
        intent.putExtra("catagory", "Maven")
        startActivity(intent)
    }
    fun navigateUFT(view: View) {
        val intent = Intent(this, Content::class.java).apply{}
        intent.putExtra("catagory", "UFT")
        startActivity(intent)
    }

}
