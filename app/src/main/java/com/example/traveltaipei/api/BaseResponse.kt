package com.example.traveltaipei.api

import kotlin.properties.Delegates

class BaseResponse<T : Any> {
    var total by Delegates.notNull<Int>()

    lateinit var data: MutableList<T>
}