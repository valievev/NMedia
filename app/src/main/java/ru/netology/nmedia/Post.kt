package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    var likeByMe: Boolean = false,
    var countLikes: Int = 0,
    var countReposts: Int = 0
)
