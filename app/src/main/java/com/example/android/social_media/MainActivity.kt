package com.example.android.social_media

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab:FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(this,CreatePost::class.java)
            startActivity(intent)
        }

    }
}