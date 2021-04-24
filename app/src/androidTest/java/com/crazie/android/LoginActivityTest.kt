package com.crazie.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.crazie.android.activity.LoginActivity
import com.crazie.android.model.User
import org.junit.Rule
import org.junit.Test



class LoginActivityTest{

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)


    @Test
    fun testLogin() {

        onView(withId(R.id._test_login_email)).perform(closeSoftKeyboard(),typeText("mail@mail.com"))
        onView(withId(R.id._test_login_password)).perform(closeSoftKeyboard(),typeText("12345"))

        onView(withId(R.id.btn_login)).perform(closeSoftKeyboard(),click())

    }

    @Test
    fun testForgotPassword(){
        onView(withId(R.id.tv_forgot_password)).perform(click())
    }


    @Test
    fun testRegistration(){
        onView(withId(R.id.tv_create_account)).perform(click())

        onView(withId(R.id._test_register_full_name)).perform(closeSoftKeyboard(), typeText(User().fullName))
        onView(withId(R.id._test_register_email)).perform(closeSoftKeyboard(), typeText(User().email))
        onView(withId(R.id._test_register_password)).perform(closeSoftKeyboard(), typeText("12345"))
        onView(withId(R.id._test_register_confirm_password)).perform(closeSoftKeyboard(), typeText("12345"))

        onView(withId(R.id.btn_create_account)).perform(closeSoftKeyboard(),click())
    }

}