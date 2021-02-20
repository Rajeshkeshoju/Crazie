package com.crazie.android.model

data class User (
    var uId:String? = "",
    var fullName:String? = "" ,
    var userName:String? = "" ,
    var email:String?  = "",
    var bio:String?  = "null",
    var photoUrl:String? = "null",
    var isEmailVerified:Boolean  = false,
    var isAccountVerified:Boolean = false,)