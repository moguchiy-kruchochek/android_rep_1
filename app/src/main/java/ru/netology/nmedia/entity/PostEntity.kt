package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val published: Long,
    val content: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val share: Int,
    val views: Int,
    val video: String? = null,
    val isDeleted: Boolean = false
) {
    fun toDto() = Post(id, author, published, content, likedByMe, likes, share, views, video, isDeleted)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            id = post.id,
            author = post.author,
            published=System.currentTimeMillis(),
            content = post.content,
            likedByMe = post.likedByMe,
            likes = post.likes,
            share = post.share,
            views = post.views,
            video = post.video,
            isDeleted = post.isDeleted
        )
    }
}