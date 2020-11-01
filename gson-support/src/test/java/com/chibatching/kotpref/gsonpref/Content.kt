package com.chibatching.kotpref.gsonpref

import com.google.gson.annotations.SerializedName
import java.util.Date

internal data class Content(

    @SerializedName("title")
    var title: String = "",

    @SerializedName("body")
    var body: String = "",

    @SerializedName("created_at")
    var createdAt: Date = Date()
)
