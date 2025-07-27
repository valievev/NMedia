package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.objects.PostService

typealias OnItemLikeListener = (post: Post) -> Unit
typealias OnItemRepostListener = (post: Post) -> Unit

class PostAdapter(
    private val onItemLikeListener: OnItemLikeListener,
    private val onItemRepostListener: OnItemRepostListener
) :
    RecyclerView.Adapter<PostViewHolder>() {

    var list: List<Post> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onItemLikeListener, onItemRepostListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post: Post = list[position]
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onItemLikeListener: OnItemLikeListener,
    private val onItemRepostListener: OnItemRepostListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countLike.text = PostService.shortCount(post.countLikes)
            countRepost.text = PostService.shortCount(post.countReposts)
            imageLike.setImageResource(
                if (post.likeByMe)
                    R.drawable.ic_liked_24
                else
                    R.drawable.ic_like_24
            )
            imageLike.setOnClickListener {
                onItemLikeListener(post)

            }
            imageRepost.setOnClickListener {
                onItemRepostListener(post)
            }
        }.root
    }
}