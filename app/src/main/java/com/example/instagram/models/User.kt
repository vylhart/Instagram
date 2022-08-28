package com.example.instagram.models

data class User(val uid: String = "",
                val userName: String? = "",
                val imageUrl: String = "",
                val bio: String = "",
                val followers: ArrayList<String> = ArrayList(),
                val followings: ArrayList<String> = ArrayList()
)
