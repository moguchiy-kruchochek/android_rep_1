package ru.netology.nmedia.postsadapter

import android.view.LayoutInflater
import android.view.View
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
    fun onOpenWebPage(url: String?)
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

        if (post.video != null) groupVideoPreview.visibility = View.VISIBLE

        likesButton.isChecked =  post.likedByMe
        likesButton.text = countersFormatting.toShorted(post.likes)
        shareButton.text = countersFormatting.toShorted(post.shared)
        viewsIcon.text = countersFormatting.toShorted(post.views)

        likesButton.setOnClickListener {
            onInteractionListener.onLike(post)
        }

        shareButton.setOnClickListener {
            onInteractionListener.onShare(post)
        }

        videoPreview.setOnClickListener {
            onInteractionListener.onOpenWebPage(post.video)
        }

        playButton.setOnClickListener {
            onInteractionListener.onOpenWebPage(post.video)
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