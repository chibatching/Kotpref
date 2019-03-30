package com.chibatching.kotprefsample

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Avatar(
    @SerializedName("icon")
    var icon: String = "default",

    @SerializedName("updated_at")
    var updatedAt: Date = Date()
)
