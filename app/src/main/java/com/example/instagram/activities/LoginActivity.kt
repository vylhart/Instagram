package com.example.instagram.activities

import android.app.Activity
import com.example.instagram.Utils.Companion.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        Log.d(TAG, "signIn: ")
        val signInIntent =  client.signInIntent
        getResult.launch(signInIntent)
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
        GlobalScope.launch(Dispatchers.IO){
            firebaseUser?.let {
                val userDao = UserDao.UserSingleton.INSTANCE
                userDao.addUser(firebaseUser)
            }
            withContext(Dispatchers.Main){
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Signed In", Toast.LENGTH_SHORT).show()
                startActivity(Intent(application, MainActivity::class.java))
                finish()
            }
        }
    }
}