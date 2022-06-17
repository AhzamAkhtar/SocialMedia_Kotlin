package com.example.android.social_media.daos

import com.example.android.social_media.models.Post
import com.example.android.social_media.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth

    fun addpost(text:String){
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch{
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)!!

            val currTime = System.currentTimeMillis()
            val post  = Post(text,user,currTime)
            postCollection.document().set(post)

        }
    }

}