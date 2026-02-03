package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textToEdit = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        binding.newPostContent.setText(textToEdit)

        binding.save.setOnClickListener {
            val text = binding.newPostContent.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val intent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                setResult(RESULT_OK, intent)
            }
            finish()
        }
    }
}

object NewPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, NewPostActivity::class.java).apply {
            if (input != null) putExtra(Intent.EXTRA_TEXT, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
}
