package ru.netology.nmedia.repository

import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getData() = dao.getAll().map { list ->
        list.map { it.toDto() }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun share(id: Int) {
        dao.share(id)
    }

    override fun softDeleteById(id: Int) {
        dao.softDeleteById(id)
    }

    override fun hardDeleteById(id: Int) {
        dao.hardDeleteById(id)
    }
}