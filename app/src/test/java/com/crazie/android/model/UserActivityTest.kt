package com.crazie.android.model

import org.junit.Assert
import org.junit.Test

class UserActivityTest {

    @Test
    fun getOtherUserImage() {
        Assert.assertNotNull(UserActivity().otherUserImage)
    }

    @Test
    fun setOtherUserImage() {
        Assert.assertNotNull(UserActivity().otherUserImage)
    }

    @Test
    fun getOtherUserPost() {
        Assert.assertNotNull(UserActivity().otherUserPost)
    }

    @Test
    fun setOtherUserPost() {
        Assert.assertNotNull(UserActivity().otherUserImage)
    }

    @Test
    fun getActivityText() {
        Assert.assertNotNull(UserActivity().activityText)
    }

    @Test
    fun setActivityText() {
        Assert.assertNotNull(UserActivity().activityText)
    }
}