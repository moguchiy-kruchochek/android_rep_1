package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilesImpl

private val empty = Post(
    id = 0,
    author = "Netology",
    published = "Now",
    content = "",
    likedByMe = false,
    likes = 0,
    shared = 0,
    views = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFilesImpl(application)

    val data = repository.getData()

    val editedPost = MutableLiveData(empty)

    fun likeById(id: Int) {
        repository.likeById(id)
    }

    fun share(id: Int) {
        repository.share(id)
    }

    fun edit(post: Post) {
        editedPost.value = post
    }

    fun save(text: String) {
        editedPost.value?.let {
                if (it.content != text) {
                    repository.save(it.copy(content = text.trim()))
            }
        }
        editedPost.value = empty
    }

    fun cancelEdit() {
        editedPost.value = empty
    }
    fun removeById(id: Int) {
        repository.removeById(id)
    }
    fun restoreById(id: Int) {
        repository.restoreById(id)
    }
}