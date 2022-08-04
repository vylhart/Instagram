package com.example.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.instagram.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val SIGN_IN = 1

    private lateinit var binding: ActivityLoginBinding
    private var client: GoogleSignInClient? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = LoginUtils.getClient(this)
        binding.googlelogin.setOnClickListener { signIn() }
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        if(user!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(){
        Log.d(TAG, "signIn: ")
        val intent = client?.signInIntent
        startActivityForResult(intent, SIGN_IN)
    }

    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: ")
        if(requestCode==SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                try{
                    var account = task.getResult(ApiException::class.java)
                    if(account!=null){
                        var authCred  =GoogleAuthProvider.getCredential(account.idToken,null)
                        auth.signInWithCredential(authCred)
                            .addOnSuccessListener {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener {
                                Log.d(TAG, "onActivityResult: failed")
                            }
                        }
                    }
                catch (e: ApiException){
                    e.printStackTrace()
                }
            }
        }
    }
}