package com.example.traveltaipei.data.events

data class CalendarBody(
    val address: String,
    val begin: String,
    val description: String,
    val distric: String,
    val elong: String,
    val end: String,
    val fax: String,
    val files: List<Any>,
    val id: Int,
    val is_major: Boolean,
    val links: List<Any>,
    val modified: String,
    val nlat: String,
    val parking: String,
    val posted: String,
    val tel: String,
    val ticket: String,
    val title: String,
    val traffic: String,
    val url: String
)