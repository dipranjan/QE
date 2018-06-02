package com.qualityengineer.dearc.qe

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.squareup.picasso.Picasso


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
            supportActionBar?.setDisplayShowTitleEnabled(false)

            val uri = Uri.parse(intent.getStringExtra("uri"))
            Picasso.with(applicationContext)
                    .load(uri)
                    .transform(PicassoCircleTransformation())
                    .resize(100, 100)
                    .centerCrop()
                    .into(findViewById<ImageView>(R.id.profilepic))



    }
    fun navigateLR(view: View) {
        val intent = Intent(this, LearningResource::class.java).apply{}
        startActivity(intent)
    }
}
