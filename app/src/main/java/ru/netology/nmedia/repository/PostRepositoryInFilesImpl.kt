package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryInFilesImpl(private val context: Context) : PostRepository {


    private var posts = listOf<Post>()
        set(value) {
            field = value
            sync()
        }


    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        }
    }

    private fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Int) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likeByMe = !post.likeByMe,
                    countLikes = if (post.likeByMe) post.countLikes - 1 else post.countLikes + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun repostById(id: Int) {

        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    countReposts = post.countReposts + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun deleteById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0) {
            listOf(post.copy(id = getNextId(), author = "me", published = "now")) + posts
        } else {
            posts.map {
                if (it.id == post.id) {
                    it.copy(content = post.content)
                } else {
                    it
                }
            }
        }
        data.value = posts
    }

    private fun getNextId(): Int {
        var nextId: Int = 0
        if (posts.isNotEmpty())
            nextId = posts.maxOf { it.id }
        return ++nextId
    }


    companion object {
        private const val FILE_NAME = "posts.json"
        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }

}