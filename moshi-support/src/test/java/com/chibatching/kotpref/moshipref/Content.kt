package com.chibatching.kotpref.moshipref

import com.squareup.moshi.Json
import java.util.*


data class Content(

        @Json(name = "title")
        var title: String = "",

        @Json(name = "body")
        var body: String = "",

        @Json(name = "created_at")
        var createdAt: Date = Date()
)
