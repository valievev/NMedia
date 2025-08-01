package ru.netology.nmedia.interfacese

import ru.netology.nmedia.dto.Post

interface OnInteractionListener {

    fun onLike(post: Post){}
    fun onRepost(post: Post){}
    fun onDelete(post: Post){}
    fun onEdit(post: Post){}

}