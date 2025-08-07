package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfacese.OnInteractionListener
import ru.netology.nmedia.objects.EditPostResultContract
import ru.netology.nmedia.objects.NewPostResultContract
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostResultContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }

        val editPostActivity = registerForActivityResult(EditPostResultContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }

//        val adapter = PostAdapter({
//            viewModel.like(it.id)
//        }, {
//            viewModel.repost(it.id)
//        }, {
//            viewModel.delete(it.id)
//        }, {
//            viewModel.edit(it)
//        }
//        )
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostActivity.launch(post.content)
            }

            override fun onDelete(post: Post) {
                viewModel.delete(post.id)
            }

            override fun onRepost(post: Post) {

                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, R.string.chooser_repost_text.toString())
                startActivity(shareIntent)
                viewModel.repost(post.id)
            }

            override fun onOpenVideoSrc(post: Post) {
                val webpage: Uri = post.srcVideo.toUri()
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
            }
        })

        binding.listPosts.adapter = adapter

        viewModel.edited.observe(this) { post ->

        }

        //binding.viewEditInfo.visibility = View.INVISIBLE


        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.listPosts.smoothScrollToPosition(0)
                }
            }
        }

        binding.addPostContent.setOnClickListener {
            viewModel.restorePost()
            newPostLauncher.launch(Unit)
        }


    }
}