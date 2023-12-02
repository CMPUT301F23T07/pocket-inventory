package com.example.pocketinventory;


import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.rule.GrantPermissionRule.grant;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
    @LargeTest
    public class CameraTest {

    @Rule
    public IntentsTestRule<ItemAddActivity> intentsRule = new IntentsTestRule<>(ItemAddActivity.class);

    

    @Test
    public void testCameraIntent() {
        // Build an ActivityResult that will return from the camera app and set your extras (if any).
        Intent resultData = new Intent();
        resultData.putExtra(MediaStore.EXTRA_OUTPUT, "test.file.url");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Mock the camera response. Now whenever an intent is sent to the camera, Espresso will respond with the result we pass here.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        // Now click on the button in your app that launches the camera.
        onView(withId(R.id.upload_image_button)).perform(click());

        // Optionally, we can also verify that the intent to the camera has actually been sent from out.
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

        // At this point, the onActivityResult() has been called, so verify that whatever view is supposed to be displayed is indeed displayed.
    }
}



