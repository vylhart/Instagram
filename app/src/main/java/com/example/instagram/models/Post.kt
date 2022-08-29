package com.example.instagram.models

data class Post(val text: String = "",
                val createdBy: String = "",
                val createdAt: Long = 0L,
                val id: String = "",
                val imageURL: String = "",
                val likedBy: ArrayList<String> = ArrayList()
)
