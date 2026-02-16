package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    var nextID: Int = 1
    var posts = listOf(
        Post(
            id = nextID++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = 0L,
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу." +
                    " Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: " +
                    "от новичков до уверенных профессионалов. Но самое важное остаётся с нами: " +
                    "мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 999_999,
            share = 9_999,
            views = 5
        ),
        Post(
            id = nextID++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = 0L,
            content = "Многие из нас: координаторы, аспиранты и эксперты — уедут в отпуск и будут недоступны для общения." +
                    "В праздничные дни срок проверки заданий и время ответов на сообщения и письма увеличатся." +
                    "На все вопросы мы обязательно ответим после каникул. Запросы на продление дедлайнов обработаем в первые рабочие дни нового года." +
                    "Если у вас есть срочные вопросы, задайте их, пожалуйста, до 30 декабря — мы ответим на них до праздников. Надеемся, что за время каникул вы прекрасно отдохнёте." +
                    "А если планируете посвятить часть времени обучению, сосредоточьтесь на более тщательной проработке заданий, пересмотрите лекции, почитайте дополнительные материалы и профильные ресурсы. " +
                    "Желаем вам отличных праздников и замечательного отдыха!",
            likes = 999,
            share = 1,
            views = 2,
            video = "https://rutube.ru/video/e6ae7dfcf4af528bf10702d644e2b4a6/"
        ),
        Post(
            id = nextID++,
            author = "Нетология. Университет интернет-профессий будущего",
            published = 0L,
            content = "Добрый день! Напоминаю, что обучение на обучение на модуле «Разработка приложений на Kotlin» было завершено." +
                    "Техническое закрытие модуля будет осуществлено 21.01.2026 г. " +
                    "Это означает, что сдать решения практических заданий и итоговых работ по модулю после указанной даты будет невозможно. " +
                    "Если данный модуль у вас завершён, поздравляю! Для вас эта информация неактуальна. " +
                    "Материалы данного модуля будут вам доступны, к ним доступ не закрывается. " +
                    "Если вы не успели закончить обучение на модуле, но планируете его завершить на новом потоке, обратитесь, пожалуйста в чат поддержки. " +
                    "Обращение необходимо направить до даты окончания обучения на программе, в вашем случае до 17.08.2026 г. Успешного завершения модуля!",
            likes = 145,
            share = 60,
            views = 56
        )
    )

    private val data = MutableLiveData(posts)

    override fun getData(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
            )
        }
        data.value = posts
    }

    override fun share(id: Int) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(
                share = it.share + 1
            )
        }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0) {
            listOf(post.copy(id = nextID++)) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }

    override fun hardDeleteById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun softDeleteById(id: Int) {
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(isDeleted = !post.isDeleted)
        }
        data.value = posts
    }
}