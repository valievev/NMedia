package ru.netology.nmedia.objects

import kotlin.math.floor

object PostService {
    fun shortCount(value: Int): String {
        return when (value) {
            in 0..999 -> value.toString()
            in 1000..9_999 -> (floor(value.toDouble() / 100) / 10).toString() + "K"
            in 10_000..999_999 -> (floor(value.toDouble() / 1000)).toInt().toString() + "K"
            else -> (floor(value.toDouble() / 100_000) / 10).toString() + "M"
        }
    }
}