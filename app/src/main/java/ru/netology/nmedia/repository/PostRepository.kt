package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun getData(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun share(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
}