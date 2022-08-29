package com.example.instagram.daos

import com.example.instagram.models.Post
import com.example.instagram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class PostDao {
    private val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = FirebaseAuth.getInstance()
    private val userDao by lazy { UserDao.UserSingleton.INSTANCE }

    object PostSingleton{
        val INSTANCE = PostDao()
    }

    fun addPost(text: String, imageUrl: String){
        val user = userDao.currentUser
        val currentTime = System.currentTimeMillis()
        val postId = UUID.randomUUID().toString()
        val post = Post(text, user.uid, currentTime, postId, imageUrl)

        GlobalScope.launch(Dispatchers.IO){
            postCollection.document(postId).set(post)
            user.posts.add(postId)
            userDao.userCollection.document(user.uid).set(user)
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

    suspend fun getPost(postId: String): Post? {
        return postCollection.document(postId).get().await().toObject(Post::class.java)
    }


}