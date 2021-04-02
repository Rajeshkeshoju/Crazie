package com.crazie.android.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class UserActivity (
    var otherUserImage : String = "",
    var otherUserPost : String = "",
    var activityText : String = ""
)