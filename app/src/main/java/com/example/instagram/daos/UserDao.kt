package com.example.instagram.daos

import android.util.Log
import com.example.instagram.Utils.Companion.TAG
import com.example.instagram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("users")
    lateinit var currentUser: User

    object UserSingleton {
        val INSTANCE = UserDao()
    }

    suspend fun addUser(firebaseUser: FirebaseUser) {
        var user = getUser(firebaseUser.uid)
        if(user==null){
            user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
            userCollection.document(user.uid).set(user)
        }
        currentUser = user
        Log.d(TAG, "addUser: ")
    }

    suspend fun getUser(userId: String): User? {
        return userCollection.document(userId).get().await().toObject(User::class.java)
    }


    fun followUser(userId: String){
        GlobalScope.launch(Dispatchers.IO){
            val followee = getUser(userId)
            followee?.let {
                followee.followers.add(currentUser.uid)
                currentUser.followings.add(userId)
                userCollection.document(userId).set(followee)
                userCollection.document(currentUser.uid).set(currentUser)
            }
        }
    }
}