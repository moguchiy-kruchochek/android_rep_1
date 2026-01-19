package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likedByMe: Boolean = false,
    val likes: Int = 0,
    val shared: Int,
    val views: Int
)