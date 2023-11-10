package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpandLoginActivityTest {

    private TestRule autoSignOutRule = new AutoSignOutRule();

    private ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);
    @Rule
    public TestRule chain = RuleChain.outerRule(autoSignOutRule).around(activityRule);

    // US 06.01.01
    // As a user, I want a profile with a unique username.
    @Test
    public void testSignUpandLogin() throws InterruptedException {
        // Create a new user with an email and password
        onView(withId(R.id.login_email)).perform(typeText("testuser@example.com"));
        onView(withId(R.id.login_password)).perform(typeText("password"));

        try {
            // Check if the user has been logged into HomePageActivity
            onView(withId(R.id.login_button)).perform(click());
            Thread.sleep(2000); // Add a delay to wait for the view to be ready
            onView(withId(R.id.log_list)).check(matches(isDisplayed()));
        } catch (Exception e) {
            // If an exception is thrown, create a new user with the email and password
            onView(withId(R.id.signUpRedirectText)).perform(click());
            onView(withId(R.id.signup_email)).perform(typeText("testuser@example.com"));
            onView(withId(R.id.signup_password)).perform(typeText("password"));
            onView(withId(R.id.confirm_signup_password)).perform(typeText("password"));

            // Check if the user has been logged into HomePageActivity
            onView(withId(R.id.signup_button)).perform(click());
            Thread.sleep(2000); // Add a delay to wait for the view to be ready
            onView(withId(R.id.log_list)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testLogout() {
        // sign in
        onView(withId(R.id.login_email)).perform(typeText("testuser@example.com"));
        onView(withId(R.id.login_password)).perform(typeText("password"));
        onView(withId(R.id.login_button)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Switch to the user profile page
        // Take the user to the profile page
        onView(withId(R.id.navigation_profile)).perform(click());
        // Log out from the app
        onView(withId(R.id.logout)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }
}
