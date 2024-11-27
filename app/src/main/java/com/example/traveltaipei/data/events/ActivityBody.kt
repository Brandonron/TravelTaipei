package com.example.traveltaipei.data.events

data class ActivityBody(
    val address: String,
    val begin: String,
    val co_rganizer: String,
    val contact: String,
    val description: String,
    val distric: String,
    val elong: String,
    val end: String,
    val fax: String,
    val files: List<Any?>,
    val id: Int,
    val links: List<Link>,
    val modified: String,
    val nlat: String,
    val organizer: String,
    val parking: String,
    val posted: String,
    val tel: String,
    val ticket: String,
    val title: String,
    val traffic: String,
    val url: String
)