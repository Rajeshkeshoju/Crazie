package com.crazie.android.model

data class SearchUser(
        var uId:String? = "",
        var fullName:String? = "" ,
        var userName:String? = "" ,
        var email:String?  = "",
        var bio:String?  = "",
        var photoUrl:String? = "null",
        var isEmailVerified:Boolean  = false,
        var isAccountVerified:Boolean = false,)