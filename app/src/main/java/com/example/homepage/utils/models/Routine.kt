package com.example.homepage.utils.models

data class Routine(
    var image: String ? ="",
    var path: String ?= ""
)
{


    fun toMap(): Map<String, Any?> {

        return mapOf(
            "image" to image,
            "path" to path
        )
    }
}
