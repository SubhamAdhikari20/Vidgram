package com.example.vidgram

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vidgram.view.activity.authentication.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthInstrumentedTest {

    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLogin() {
        // Type email
        onView(withId(R.id.emailInputText)).perform(typeText("subham@gmail.com"))

        // Type password
        onView(withId(R.id.passwordInputText)).perform(typeText("987654321"))

        // Close the keyboard
        closeSoftKeyboard()

        // Click login button
        onView(withId(R.id.loginButton)).perform(click())

        // Wait until UI settles (replaces Thread.sleep)
        onIdle()

        // Check if error message is displayed
        onView(withId(R.id.message)).check(matches(withText("login success")))
    }

@Test
    fun failLogin() {
        // Type email
        onView(withId(R.id.emailInputText)).perform(typeText("hari@gmail.com"))

        // Type password
        onView(withId(R.id.passwordInputText)).perform(typeText("password"))

        // Close the keyboard
        closeSoftKeyboard()

        // Click login button
        onView(withId(R.id.loginButton)).perform(click())

        // Wait until UI settles (replaces Thread.sleep)
        onIdle()

        // Check if error message is displayed
        onView(withId(R.id.message)).check(matches(withText("login failed")))
    }
}
