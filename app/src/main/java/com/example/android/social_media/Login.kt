package com.example.android.social_media

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

         val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestIdToken(getString(R.string.your_web_client_id_auth))
             .requestEmail()
             .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        auth = Firebase.auth


        val button : SignInButton = findViewById(R.id.button)
        button.setOnClickListener{
            signIn()
        }

    }

    fun signIn(){
        val signinIntent = googleSignInClient.signInIntent
        startActivityForResult(signinIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try{
            val account = completedTask.getResult(ApiException::class.java)
            Log.d("tag","firebase"+ account.id)
            firebaseAuthWithGoogle(account.idToken)
        }catch (e: ApiException){
            Log.d("tag","firebaseFailed" + e.statusCode)
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        val button : SignInButton = findViewById(R.id.button)
        button.visibility = View.GONE
        val progressbar : ProgressBar = findViewById(R.id.progressBar)
        progressbar.visibility = View.VISIBLE
        GlobalScope.launch ( Dispatchers.IO){
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser!=null){
            val mainActivityIntent = Intent(this,MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }else{
            val button : SignInButton = findViewById(R.id.button)
            button.visibility = View.VISIBLE
            val progressbar : ProgressBar = findViewById(R.id.progressBar)
            progressbar.visibility = View.GONE
        }

    }
}