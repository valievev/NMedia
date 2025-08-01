package ru.netology.nmedia.activity

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfacese.OnInteractionListener
import ru.netology.nmedia.objects.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()

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
            }

            override fun onDelete(post: Post) {
                viewModel.delete(post.id)
            }

            override fun onRepost(post: Post) {
                viewModel.repost(post.id)
            }
        })

        binding.listPosts.adapter = adapter

        viewModel.edited.observe(this) { post ->
            if (post.id != 0) {
                binding.content.setText(post.content)

                //binding.viewEditInfo.visibility = View.GONE
                binding.viewEditInfo.visibility = View.VISIBLE
                binding.editViewTextviewContent.text = post.content

                //binding.content.requestFocus()
                AndroidUtils.showKeyboard(binding.content)
            }
        }

        binding.viewEditInfo.visibility = View.GONE
        //binding.viewEditInfo.visibility = View.INVISIBLE


        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.listPosts.smoothScrollToPosition(0)
                }
            }
        }

        binding.imageButtonAddEdit.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.save(content = text.toString())

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

        binding.cancelEditViewImage.setOnClickListener {
            with(binding) {
                viewEditInfo.visibility = View.GONE
                //viewEditInfo.visibility = View.INVISIBLE
                content.clearFocus()
                content.setText("")
                viewModel.cancelEdit()
            }
        }


    }
}