package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.netology.nmedia.R
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.postsadapter.OnInteractionListener
import ru.netology.nmedia.postsadapter.PostsAdapter
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    val newPostLauncher = registerForActivityResult(NewPostContract) {
        it ?: return@registerForActivityResult
        viewModel.save(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left, systemBars.top, systemBars.right, systemBars.bottom
            )
            insets
        }

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }

            override fun onOpenWebPage(url: String?) {
                val webpage: Uri? = url?.toUri()
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                newPostLauncher.launch(post.content)
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

        binding.add.setOnClickListener {
            newPostLauncher.launch(null)
        }


    }
}
