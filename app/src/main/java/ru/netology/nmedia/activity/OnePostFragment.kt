package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity.Companion.idPost
import ru.netology.nmedia.activity.AppActivity.Companion.textArg
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostViewHolder
//import ru.netology.nmedia.activity.AppActivity.Companion.postArg
import ru.netology.nmedia.databinding.FragmentOnePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfacese.OnInteractionListener
import ru.netology.nmedia.objects.AndroidUtils
import ru.netology.nmedia.objects.PostService
import ru.netology.nmedia.viewmodel.PostViewModel

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class OnePostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var binding = FragmentOnePostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )
        //binding.content.text = arguments?.idPost?.let { viewModel.getPostById(it)?.content } ?: "null"

        viewModel.data.observe(viewLifecycleOwner) {
            val onePost = arguments?.idPost?.let { viewModel.getPostById(it) }
            if (onePost != null) {
                PostViewHolder(
                    binding.post,
                    object : OnInteractionListener {
                        override fun onLike(post: Post) {
                            viewModel.like(post.id)
                        }

                        override fun onEdit(post: Post) {
                            viewModel.edit(post)
                            //editPostActivity.launch(post.content)
                            findNavController().navigate(
                                R.id.action_onePostFragment_to_addEditPostFragment,
                                Bundle().apply {
                                    textArg = post.content
                                })
                        }

                        override fun onDelete(post: Post) {
                            viewModel.delete(post.id)
                            findNavController().navigateUp()
                        }

                        override fun onRepost(post: Post) {

                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, post.content)
                                type = "text/plain"
                            }

                            val shareIntent =
                                Intent.createChooser(
                                    intent,
                                    R.string.chooser_repost_text.toString()
                                )
                            startActivity(shareIntent)
                            viewModel.repost(post.id)
                        }

                        override fun onOpenVideoSrc(post: Post) {
                            val webpage: Uri = post.srcVideo.toUri()
                            val intent = Intent(Intent.ACTION_VIEW, webpage)
                            startActivity(intent)
                        }
                    }).bind(
                    post = onePost
                )

            }
        }
        return binding.root

    }
}