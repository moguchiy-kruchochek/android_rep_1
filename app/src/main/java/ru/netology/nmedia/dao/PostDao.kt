package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Int)
    fun save(post: Post): Post
    fun share(id: Int)
    fun softDeleteById(id: Int)
    fun hardDeleteById(id: Int)
}
