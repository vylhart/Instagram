package com.example.instagram.daos

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
    private val auth = FirebaseAuth.getInstance()
    val userCollection = db.collection("users")
    lateinit var currentUser: User

    fun addUser(firebaseUser: FirebaseUser) {
        GlobalScope.launch(Dispatchers.IO) {
            var user = getUser(firebaseUser.uid)
            if(user==null){
                user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
                userCollection.document(user.uid).set(user)
            }
            currentUser = user
        }
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