package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

private val empty = Post(
    id = 0,
    author = "Netology",
    published = 0L,
    content = "",
    likedByMe = false,
    likes = 0,
    share = 0,
    views = 0
)


class PostViewModel(application: Application) : AndroidViewModel(application) {

    var draft: String? = null

    private val repository: PostRepository =
        PostRepositoryRoomImpl(AppDb.getInstance(application).postDao)

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

    fun softDeleteById(id: Int) {
        repository.softDeleteById(id)
    }

    fun hardDeleteById(id: Int) {
        repository.hardDeleteById(id)
    }
}