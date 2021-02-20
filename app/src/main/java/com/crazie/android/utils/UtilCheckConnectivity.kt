package com.crazie.android.utils

class UtilCheckConnectivity {

    fun isOnline():Boolean{
        return try {
            val command = "ping -c 1 google.com"
            (Runtime.getRuntime().exec(command).waitFor() == 0)
        }catch (e:Exception){
            false
        }
    }
}