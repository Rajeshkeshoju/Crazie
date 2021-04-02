package com.crazie.android.model

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class User (
    var uId:String? = "",
    var fullName:String? = "" ,
    var userName:String? = "" ,
    var email:String?  = "",
    var bio:String?  = "null",
    var photoUrl:String? = "null",
    var isEmailVerified:Boolean  = false,
    var isAccountVerified:Boolean = false,)