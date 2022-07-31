package com.example.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.instagram.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var client: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener { signOut() }
        client = LoginUtils.getClient(this)
    }

    override fun onStart() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            binding.textview.text = account.displayName
        }
        super.onStart()
    }

    private fun signOut(){
        Log.d(TAG, "signOut: ")
        client?.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}