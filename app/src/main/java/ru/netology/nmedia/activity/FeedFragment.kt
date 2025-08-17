package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfacese.OnInteractionListener
import ru.netology.nmedia.objects.EditPostResultContract
import ru.netology.nmedia.objects.NewPostResultContract
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.activity.AppActivity.Companion.idPost
//import ru.netology.nmedia.activity.AppActivity.Companion.postArg
import ru.netology.nmedia.activity.AppActivity.Companion.textArg
import ru.netology.nmedia.objects.IntArgs

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        //setContentView(binding.root)


        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

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
                //editPostActivity.launch(post.content)
                findNavController().navigate(
                    R.id.action_feedFragment_to_addEditPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
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

            override fun showOnePost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply {
                        idPost = post.id
                        //textArg = "lol"
                        //postArg = post
                    })
            }
        })

        binding.listPosts.adapter = adapter

        viewModel.edited.observe(viewLifecycleOwner) { post ->

        }

        //binding.viewEditInfo.visibility = View.INVISIBLE


        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.listPosts.smoothScrollToPosition(0)
                }
            }
        }

        binding.addPostContent.setOnClickListener {
            viewModel.restorePost()
            findNavController().navigate(R.id.action_feedFragment_to_addEditPostFragment)
//            newPostLauncher.launch(Unit)
        }


        return binding.root
    }
}