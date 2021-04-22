package com.crazie.android.fragment

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.crazie.android.MainActivity
import com.crazie.android.R
import com.crazie.android.model.User
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup(){
        activityRule.activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,AccountFragment(
                        FirebaseAuth.getInstance().currentUser!!.uid),"Profile")
                .commitAllowingStateLoss()

        Thread.sleep(500)
    }

    @Test
    fun onCreateView() {
        Assert.assertNotNull(User().uId)
        Assert.assertNotNull(User().userName)
        Assert.assertNotNull(User().photoUrl)
        Assert.assertNotNull(User().email)
    }
}