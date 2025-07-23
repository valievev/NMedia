package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val likeByMe: Boolean = false,
    val countLikes: Int = 0,
    val countReposts: Int = 0
)
