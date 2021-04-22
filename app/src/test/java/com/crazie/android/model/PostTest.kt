package com.crazie.android.model

import org.junit.Assert
import org.junit.Test

class PostTest {

    @Test
    fun getPostId() {
        Assert.assertNotNull(Post().postId)
    }

    @Test
    fun getPostImage() {
        Assert.assertNotNull(Post().postImage)
    }

    @Test
    fun getPostCaption() {
        Assert.assertNotNull(Post().postCaption)
    }

    @Test
    fun getPublisher() {
        Assert.assertNotNull(Post().publisher)
    }

    @Test
    fun setPostId() {
        Assert.assertNotNull(Post().postId)
    }

    @Test
    fun setPostImage() {
        Assert.assertNotNull(Post().postImage)
    }

    @Test
    fun setPostCaption() {
        Assert.assertNotNull(Post().postCaption)
    }

    @Test
    fun setPublisher() {
        Assert.assertNotNull(Post().publisher)
    }
}