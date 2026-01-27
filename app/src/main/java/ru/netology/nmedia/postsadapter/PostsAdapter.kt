package ru.netology.nmedia.postsadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.CountersFormatting
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)

}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    val countersFormatting = CountersFormatting()
    fun bind(post: Post) = with(binding) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        if (post.likedByMe) {
            likesIcon.setImageResource(R.drawable.ic_liked_24)
        } else {
            likesIcon.setImageResource(R.drawable.ic_like_24)
        }
        likesCount.text = countersFormatting.toShorted(post.likes)
        sharedCount.text = countersFormatting.toShorted(post.shared)
        viewsCount.text = countersFormatting.toShorted(post.views)

        likesIcon.setOnClickListener {
            onInteractionListener.onLike(post)
        }

        sharedIcon.setOnClickListener {
            onInteractionListener.onShare(post)
        }

        moreButton.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_options)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post) = newItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}