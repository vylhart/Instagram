package com.example.instagram.daos

import com.example.instagram.models.Post
import com.example.instagram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = FirebaseAuth.getInstance()
    val userDao by lazy { UserDao() }

    fun addPost(text: String){
        val user = userDao.currentUser
        val currentTime = System.currentTimeMillis()
        val postId = UUID.randomUUID().toString()
        val post = Post(text, user.uid, currentTime, postId)

        GlobalScope.launch(Dispatchers.IO){
            postCollection.document(postId).set(post)
        }
        notifyPost(postId)
    }

    private fun notifyPost(postId: String){
        for(followerId in userDao.currentUser!!.followers){
            GlobalScope.launch(Dispatchers.IO){
                val follower = userDao.getUser(followerId)
                follower?.let {
                    follower.timeline.add(postId)
                    userDao.userCollection.document(followerId).set(follower)
                }
            }
        }
    }


}