package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.getData()

    fun likeById(id: Int) {
        repository.likeById(id)
    }

    fun share(id: Int) {
        repository.share(id)
    }
}