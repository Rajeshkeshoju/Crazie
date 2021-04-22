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
        Assert.assertNotNull(User().uId)
    }

    @Test
    fun getFullName() {
        Assert.assertNotNull(User().fullName)
    }

    @Test
    fun setFullName() {
        Assert.assertNotNull(User().fullName)
    }

    @Test
    fun getUserName() {
        Assert.assertNotNull(User().userName)
    }

    @Test
    fun setUserName() {
        Assert.assertNotNull(User().userName)
    }

    @Test
    fun getEmail() {
        Assert.assertNotNull(User().email)
    }

    @Test
    fun setEmail() {
        Assert.assertNotNull(User().email)
    }

    @Test
    fun getBio() {
        Assert.assertNotNull(User().bio)
    }

    @Test
    fun setBio() {
        Assert.assertNotNull(User().bio)
    }

    @Test
    fun getPhotoUrl() {
        Assert.assertNotNull(User().photoUrl)
    }

    @Test
    fun setPhotoUrl() {
        Assert.assertNotNull(User().photoUrl)
    }

    @Test
    fun isEmailVerified() {
        Assert.assertNotNull(User().isEmailVerified)
        Assert.assertEquals(false,User().isEmailVerified)
    }

    @Test
    fun setEmailVerified() {
        Assert.assertNotNull(User().isEmailVerified)
    }

    @Test
    fun isAccountVerified() {
        Assert.assertNotNull(User().isAccountVerified)
        Assert.assertEquals(false,User().isAccountVerified)
    }

    @Test
    fun setAccountVerified() {
        Assert.assertNotNull(User().isAccountVerified)
    }
}