package com.example.android.social_media

import PostDao
import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.social_media.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.RepoManager.clear
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), IPostAdapter {
    //private lateinit var adapter1:PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setUpAdapter()

        val signOutButton: Button = findViewById(R.id.buttonSignOut)
        signOutButton.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete")
            builder.setMessage("Are You Sure")
            builder.setIcon(R.drawable.ic_logout)
            builder.setPositiveButton("Yes"){
                dialogeInterface,which ->

                //val auth = Firebase.auth
                //auth.signOut()
                FirebaseAuth.getInstance().signOut()
                var auth = FirebaseAuth.getInstance()
                auth.signOut()
                finish()
                val intent:Intent = Intent(this,Login::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
            }
            builder.setNegativeButton("No"){
                    dialogeInterface,which ->
                val snackbar = Snackbar.make(it,"Logout Cancel", Snackbar.LENGTH_LONG).show()
            }
            val alertDialog:AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

        val fab:FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(this,CreatePost::class.java)
            startActivity(intent)
        }
        setUpAdapter()

    }

    private fun setUpAdapter() {
        var postDao=PostDao()
        val postCollections = postDao.postCollections
        val query = postCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()


        val  adapter = PostAdapter(recyclerViewOption,this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.startListening()
    }

    override fun onLikeClicked(postId: String) {
        var postDao=PostDao()
        postDao.updateLikes(postId)
    }

    /**override fun onStart() {
        super.onStart()

        adapter1.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter1.stopListening()
    }**/
}