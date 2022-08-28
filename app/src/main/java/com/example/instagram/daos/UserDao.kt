package com.example.instagram.daos

import com.example.instagram.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun addUser(firebaseUser: FirebaseUser) {
        val user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
        GlobalScope.launch(Dispatchers.IO) {
            userCollection.document(user.uid).set(user)
        }
    }
}