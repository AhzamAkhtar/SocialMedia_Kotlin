package com.example.android.social_media

import PostDao
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import com.example.android.social_media.daos.UserDao

class CreatePost : AppCompatActivity() {

    //private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val postButton : Button = findViewById(R.id.postButton)
        postButton.setOnClickListener{
            val intput : EditText = findViewById(R.id.postInput)
            val input_text = intput.text.toString().trim()
            if(input_text.isNotEmpty()){
                var postDao = PostDao()
                postDao.addPost(input_text)
                finish()
            }
        }

    }
}