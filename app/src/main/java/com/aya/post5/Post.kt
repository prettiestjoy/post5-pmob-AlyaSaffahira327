package com.aya.post5

import android.net.Uri

data class Post(
    val id: Long,
    var username: String,
    var caption: String,

    var postImageUri: Uri? = null,

    var postImageResId: Int? = null,

    val profilePicResId: Int
)