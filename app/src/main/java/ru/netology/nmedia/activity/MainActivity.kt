package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.objects.PostService
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(binding.main){v,insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        //var postWorker: PostWorker

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countLike.text = PostService.shortCount(post.countLikes)
                countRepost.text = PostService.shortCount(post.countReposts)
                imageLike.setImageResource(
                    if (post.likeByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )

            }
        }
        binding.imageLike.setOnClickListener {
            viewModel.like()
        }
        binding.imageRepost.setOnClickListener {
            viewModel.repost()
        }
//        val post1 = Post(
//            id = 1,
//            author = "Нетология. Университет интернет-профессий будущего",
//            authorAvatar = "@sample/posts_avatars",
//            published = "21 мая в 18:36",
//            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            countReposts = 1_099_990,
//            countLikes = 14_999
//        )
//        with(binding) {
//            author.text = post1.author
//            published.text = post1.published
//            content.text = post1.content
//            countLike.text = PostService.shortCount(post1.countLikes)
//            imageLike.setImageResource(R.drawable.ic_like_24)
//            countRepost.text = PostService.shortCount(post1.countReposts)
//
//            imageLike.setOnClickListener {
//                post1.likeByMe = !post1.likeByMe;
//                if (post1.likeByMe) {
//                    imageLike.setImageResource(R.drawable.ic_liked_24)
//                    post1.countLikes++
//                } else {
//                    imageLike.setImageResource(R.drawable.ic_like_24)
//                    post1.countLikes--
//                }
//                countLike.text = PostService.shortCount(post1.countLikes)
//            }
//
//            imageRepost.setOnClickListener {
//                countRepost.text = PostService.shortCount(++post1.countReposts)
//
//
//            }
//        }
    }
}


//        println(R.string.hello) // число
//        println(getString(R.string.hello)) // "Привет, Мир!" или "Hello World!"
