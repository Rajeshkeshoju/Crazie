package com.crazie.android.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Post(val postId:String = "",
                val postImage:String = "",
                val postCaption:String = "",
                val publisher:String = "")