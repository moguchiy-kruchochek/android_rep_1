package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {

    var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }


    override fun getData(): LiveData<List<Post>> = data


    override fun likeById(id: Int) {
        dao.likeById(id)
        posts = posts.map { post ->
            if (post.id != id) post
            else post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
        }
        data.value = posts
    }

    override fun share(id: Int) {
        dao.share(id)
        posts = posts.map {
            if (it.id != id) it
            else it.copy(
                share = it.share + 1
            )
        }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun hardDeleteById(id: Int) {
        dao.hardDeleteById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun softDeleteById(id: Int) {
        dao.softDeleteById(id)
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(isDeleted = !post.isDeleted)
        }
        data.value = posts
    }
}