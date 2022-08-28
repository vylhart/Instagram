package com.example.instagram.activities

import com.example.instagram.Utils.Companion.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.instagram.R
import com.example.instagram.daos.UserDao
import com.example.instagram.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate: ")
        binding.loginBtn.setOnClickListener { signIn() }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    private fun signIn() {
        Log.d(TAG, "signIn: ")
        val signInIntent =  client.signInIntent
        startActivityForResult(signInIntent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try{
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "handleSignInResult: "+account.idToken)
            firebaseAuthWithGoogle(account.idToken)
        } catch (e: ApiException){
            Log.w(TAG, "handleSignInResult: error")
            e.printStackTrace()
        }
    }

    private fun firebaseAuthWithGoogle(id: String?) {
        val cred = GoogleAuthProvider.getCredential(id, null)
        binding.progressBar.visibility = View.VISIBLE
        binding.loginBtn.isEnabled = false
        auth.signInWithCredential(cred).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.d(TAG, "firebaseAuthWithGoogle: success")
                val user = auth.currentUser
                updateUI(user)
            }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        Log.d(TAG, "updateUI: ")
        binding.progressBar.visibility = View.INVISIBLE
        firebaseUser?.let {
            val userDao = UserDao()
            userDao.addUser(firebaseUser)
            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}