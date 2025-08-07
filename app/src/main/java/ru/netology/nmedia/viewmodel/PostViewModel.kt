package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl


class PostViewModel : ViewModel() {

    private var emptyPost = Post(
        id = 0,
        author = "",
        authorAvatar = "",
        published = "",
        content = ""
    )


    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(emptyPost)

    fun save(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (text != it.content) {
                repository.save(it.copy(content = text))
            }
        }
        edited.value = emptyPost
    }


    fun edit(post: Post) {
        edited.value = post
    }

    fun restorePost() {
        edited.value = emptyPost;
    }


    fun like(id: Int) = repository.likeById(id)
    fun repost(id: Int) = repository.repostById(id)
    fun delete(id: Int) = repository.deleteById(id)
}