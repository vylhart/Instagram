package com.example.instagram.models


data class User(val uid: String = "",
                val userName: String? = "",
                val imageUrl: String = "",
                val bio: String = "",
                val posts: HashMap<String, Post> = HashMap(),
                val followers: HashMap<String, User> = HashMap(),
                val followings: HashMap<String, User> = HashMap(),
                val timeline: HashMap<String, Post> = HashMap()
)
