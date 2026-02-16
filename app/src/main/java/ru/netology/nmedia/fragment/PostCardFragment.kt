package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.CountersFormatting
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.fragment.NewPostFragment.Companion.textArg
import ru.netology.nmedia.util.IdArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {

    val countersFormatting = CountersFormatting()

    companion object {
        var Bundle.idArg by IdArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(layoutInflater)
        val viewModel: PostViewModel by activityViewModels()
        var post = Post(
            id = 0,
            author = "",
            published = "",
            content = "",
            likedByMe = false,
            likes = 0,
            shared = 0,
            views = 0
        )

        val postId = arguments?.idArg

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            post = posts.firstOrNull { it.id == postId }
                ?: run {
                    Snackbar.make(
                        binding.root,
                        "Пост не найден или был удален",
                        Snackbar.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                    return@observe
                }
            post.let {
                with(binding.includedPostCard) {
                    author.text = it.author
                    published.text = it.published
                    content.text = it.content

                    content.maxLines = Int.MAX_VALUE

                    likesButton.isChecked = it.likedByMe

                    likesButton.text = countersFormatting.toShorted(it.likes)
                    shareButton.text = countersFormatting.toShorted(it.shared)
                    viewsIcon.text = countersFormatting.toShorted(it.views)

                    if (it.video != null) groupVideoPreview.visibility = View.VISIBLE
                    else groupVideoPreview.visibility = View.GONE
                }
            }
        }

        with(binding.includedPostCard) {
            likesButton.setOnClickListener { viewModel.likeById(post.id) }

            shareButton.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }

            moreButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)

                    menu.findItem(R.id.restore).isVisible = false

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {

                            R.id.edit -> {
                                viewModel.edit(post)
                                findNavController().navigate(
                                    R.id.action_postCardFragment_to_newPostFragment,
                                    Bundle().apply { textArg = post.content }
                                )
                                true
                            }

                            R.id.remove -> {
                                viewModel.removeById(post.id)
                                Snackbar.make(
                                    binding.root,
                                    "Пост не найден или был удален",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                findNavController().popBackStack()
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }

        return binding.root

    }
}


