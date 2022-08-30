package com.example.instagram.models

data class User(val uid: String = "",
                val userName: String? = "",
                val imageUrl: String = "",
                val bio: String = "",
                val posts: ArrayList<String> = ArrayList(),
                val followers: ArrayList<String> = ArrayList(),
                val followings: ArrayList<String> = ArrayList(),
                val timeline: HashMap<String, Post> = HashMap()
)
