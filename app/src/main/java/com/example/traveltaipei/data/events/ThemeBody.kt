package com.example.traveltaipei.data.events

data class ThemeBody(
    val author: String,
    val category: Any,
    val consume: String,
    val days: Int,
    val description: String,
    val files: List<Any>,
    val id: Int,
    val modified: String,
    val months: List<String>,
    val note: String,
    val remark: String,
    val seasons: List<String>,
    val title: String,
    val transport: Any,
    val url: String,
    val users: Any
)