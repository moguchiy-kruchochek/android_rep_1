package ru.netology.nmedia.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.postsadapter.OnInteractionListener
import ru.netology.nmedia.postsadapter.PostViewHolder
import ru.netology.nmedia.util.IdArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {

    val viewModel: PostViewModel by activityViewModels()

    private val onInteractionListener = object : OnInteractionListener {
        override fun onLike(post: Post) {
            viewModel.likeById(post.id)
        }

        override fun onShare(post: Post) {
            viewModel.share(post.id)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(chooser)
        }

        override fun onEdit(post: Post) {
            viewModel.edit(post)
        }

        override fun onSoftDelete(id: Int) {
            viewModel.softDeleteById(id)
        }

        override fun onOpenWebPage(url: String?) {
            val webpage: Uri? = url?.toUri()
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(intent)
        }

        override fun onPostOpen(post: Post) {}

        override fun onHardDelete(id: Int) {}
    }

    companion object {
        var Bundle.idArg by IdArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(layoutInflater)
        val postViewHolder = PostViewHolder(
            binding = binding.includedPostCard,
            onInteractionListener
        )
        val postId = arguments?.idArg

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.firstOrNull { it.id == postId }
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
                if (post.isDeleted) findNavController().popBackStack()
                postViewHolder.bind(post)
            }
        }
        return binding.root
    }
}


