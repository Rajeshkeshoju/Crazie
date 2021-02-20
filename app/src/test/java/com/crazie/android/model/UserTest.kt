package com.crazie.android.model

import org.junit.Assert
import org.junit.Test

class UserTest {
    @Test
    fun getUId() {
        Assert.assertNotNull(User().uId)
    }

    @Test
    fun setUId() {
    }

    @Test
    fun getFullName() {
    }

    @Test
    fun setFullName() {
    }

    @Test
    fun getUserName() {
        Assert.assertNotNull(User().userName)
    }

    @Test
    fun setUserName() {
    }

    @Test
    fun getEmail() {
    }

    @Test
    fun setEmail() {
    }

    @Test
    fun getBio() {
    }

    @Test
    fun setBio() {
    }

    @Test
    fun getPhotoUrl() {
    }

    @Test
    fun setPhotoUrl() {
    }

    @Test
    fun isEmailVerified() {
        Assert.assertEquals(false,User().isEmailVerified)
    }

    @Test
    fun setEmailVerified() {
    }

    @Test
    fun isAccountVerified() {
    }

    @Test
    fun setAccountVerified() {
    }
}