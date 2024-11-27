package com.example.traveltaipei.data.events

data class NewsBody(
    val begin: Any,
    val description: String,
    val end: Any,
    val files: List<Any>,
    val id: Int,
    val links: List<Link>,
    val modified: String,
    val posted: String,
    val title: String,
    val url: String
)