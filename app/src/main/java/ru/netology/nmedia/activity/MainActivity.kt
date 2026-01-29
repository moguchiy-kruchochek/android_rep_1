package ru.netology.nmedia.activity

import ru.netology.nmedia.R
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.postsadapter.OnInteractionListener
import ru.netology.nmedia.postsadapter.PostsAdapter
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.share(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

        }
        )
        binding.recyclerContainer.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.recyclerContainer.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.editedPost.observe(this) { post ->
            if (post.id != 0) {
                binding.editingText.text = post.content
                binding.newPostContent.setText(post.content)
                AndroidUtils.showKeyboard(binding.newPostContent)
                binding.editTextGroup.visibility = View.VISIBLE
            }
        }

        binding.saveButton.setOnClickListener {
            val text = binding.newPostContent.text.toString()
            if (text.isBlank()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                AndroidUtils.hideKeyboard(binding.newPostContent)
                binding.newPostContent.clearFocus()
                return@setOnClickListener
            }
            viewModel.save(text)
            with(binding) {
                editingText.text = ""
                newPostContent.setText("")
                newPostContent.clearFocus()
                AndroidUtils.hideKeyboard(newPostContent)
                editTextGroup.visibility = View.GONE
            }
        }

        with(binding) {
            cancelButton.setOnClickListener {
                viewModel.cancelEdit()
                newPostContent.setText("")
                editingText.text = ""
                newPostContent.clearFocus()
                AndroidUtils.hideKeyboard(newPostContent)
                editTextGroup.visibility = View.GONE
            }
        }
    }
}
