package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft + systemBars.left,
                v.paddingTop + systemBars.top,
                v.paddingRight + systemBars.right,
                v.paddingBottom + systemBars.bottom
            )
            insets
        }
        val countFormatter = CountFormatter()
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "14 января в 16:00",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу." +
                    " Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: " +
                    "от новичков до уверенных профессионалов. Но самое важное остаётся с нами: " +
                    "мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 999_999,
            shared = 9_999,
            views = 5
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            if (post.likedByMe) {
                likesIcon.setImageResource(R.drawable.ic_liked_24)
            }
            likesCount.text = countFormatter.numbersToShorted(post.likes)
            sharedCount.text = countFormatter.numbersToShorted(post.shared)
            viewsCount.text = countFormatter.numbersToShorted(post.views)

            likesIcon.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likesIcon.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                if (post.likedByMe) post.likes += 1 else post.likes -= 1
                likesCount.text = countFormatter.numbersToShorted(post.likes)
            }

            sharedIcon.setOnClickListener {
                post.shared += 1
                sharedCount.text = countFormatter.numbersToShorted(post.shared)
            }
        }

    }
}