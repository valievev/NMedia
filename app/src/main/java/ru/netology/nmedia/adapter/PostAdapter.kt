package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.interfacese.OnInteractionListener
import ru.netology.nmedia.objects.PostDiffCallback
import ru.netology.nmedia.objects.PostService

typealias OnItemLikeListener = (post: Post) -> Unit
typealias OnItemRepostListener = (post: Post) -> Unit
typealias OnItemRemoveListener = (post: Post) -> Unit
typealias OnItemEditListener = (post: Post) -> Unit

class PostAdapter(
    private val onInteraction: OnInteractionListener
//    private val onItemLikeListener: OnItemLikeListener,
//    private val onItemRepostListener: OnItemRepostListener,
//    private val onItemRemoveListener: OnItemRemoveListener,
//    private val onItemEditListener: OnItemEditListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding,
            onInteraction
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteraction: OnInteractionListener
//    private val onItemLikeListener: OnItemLikeListener,
//    private val onItemRepostListener: OnItemRepostListener,
//    private val onItemRemoveListener: OnItemRemoveListener,
//    private val onItemEditListener: OnItemEditListener

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeButton.text = PostService.shortCount(post.countLikes)
            repostButton.text = PostService.shortCount(post.countReposts)
            likeButton.isChecked = post.likeByMe
            if (post.srcVideo.isNotBlank()) {
                //android:src="@drawable/sector_gaza"
                videoImagePreView.visibility = View.VISIBLE
                videoImagePreView.setImageResource(R.drawable.sector_gaza)
            }
            else{
                videoImagePreView.visibility = View.GONE
            }
//            likeButton.icon =
//                if (post.likeByMe)
//                    R.drawable.ic_liked_24
//                else
//                    R.drawable.ic_like_24
//            )

            videoImagePreView.setOnClickListener(){
                onInteraction.onOpenVideoSrc(post)
            }

            likeButton.setOnClickListener {
                onInteraction.onLike(post)

            }
            repostButton.setOnClickListener {
                onInteraction.onRepost(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteraction.onDelete(post)
                                true
                            }

                            R.id.edit -> {
                                onInteraction.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }.root
    }
}