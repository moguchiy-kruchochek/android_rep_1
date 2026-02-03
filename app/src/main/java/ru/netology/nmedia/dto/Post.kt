package ru.netology.nmedia.dto

import java.net.URL

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shared: Int,
    val views: Int,
    val video: String? = null
)