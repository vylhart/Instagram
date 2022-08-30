package com.example.instagram.daos

import android.util.Log
import com.example.instagram.Utils.Companion.TAG
import com.example.instagram.models.Post
import com.example.instagram.models.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    lateinit var currentUser: User

    object UserSingleton {
        val INSTANCE = UserDao()
    }

    suspend fun addUser(firebaseUser: FirebaseUser) {
        Log.d(TAG, "addUser: ")
        var user = getUser(firebaseUser.uid)
        if(user==null){
            user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
            userCollection.document(user.uid).set(user)
        }
        currentUser = user
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

    fun addPost(text: String, imageUrl: String){
        val currentTime = System.currentTimeMillis()
        val postId = UUID.randomUUID().toString()
        val post = Post(text, currentUser.uid, currentTime, postId, imageUrl)

        GlobalScope.launch(Dispatchers.IO){
            userCollection.document(currentUser.uid).collection("posts").document(postId).set(post)
            userCollection.document(currentUser.uid).collection("timeline").document(postId).set(post)
        }
        notifyPost(post)
    }

    private fun notifyPost(post: Post){
        for(followerId in currentUser.followers){
            GlobalScope.launch(Dispatchers.IO){
                val follower = getUser(followerId)
                follower?.let {
                    userCollection.document(followerId).collection("timeline").document(post.id).set(post)
                }
            }
        }
    }

    fun getOptions(collectionName: String): FirestoreRecyclerOptions<Post> {
        val collection = userCollection.document(currentUser.uid).collection(collectionName)
        val query = collection.orderBy("createdAt", Query.Direction.DESCENDING)
        return FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
    }

}