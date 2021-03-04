package com.example.aplicacionfinalkotlin.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionfinalkotlin.R

class FirstActivity : AppCompatActivity() {
    lateinit var text: TextView
    lateinit var image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        text = findViewById<View>(R.id.tv_loading) as TextView
        image = findViewById<View>(R.id.imageView) as ImageView

        val load = View.OnClickListener {view ->
            startActivity(Intent(this, LoginActivity::class.java))
        }
        text.setOnClickListener(load)
        image.setOnClickListener(load)

    }
}