package com.example.pocketinventory;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.DatePicker;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
    @LargeTest
    public class CameraAndGalleryTest {

    @Rule
    public IntentsTestRule<ItemAddActivity> intentsRule = new IntentsTestRule<>(ItemAddActivity.class);

    public static void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(withParent(withId(R.id.date_of_purchase_text))).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void testCameraIntent() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Apple"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("iPhone 12"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1000"));
        CameraAndGalleryTest.setDate(R.id.date_of_purchase_edit_text, 2023, 9, 15);
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("Blue"));
        Espresso.onView(withId(R.id.tag_edit_text)).perform(typeText("expensive, fast, durable"));

        onView(withId(R.id.upload_image_button)).perform(click());
        // Build an ActivityResult that will return from the camera app and set your extras (if any).
        Intent resultData = new Intent();
        resultData.putExtra(MediaStore.EXTRA_OUTPUT, "test.file.url");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Mock the camera response. Now whenever an intent is sent to the camera, Espresso will respond with the result we pass here.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        // Now click on the button in your app that launches the camera.
        onView(withText("Camera")).perform(click());

        // Optionally, we can also verify that the intent to the camera has actually been sent from out.
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

        // Check that the image is displayed
        Espresso.onView(withId(R.id.carousel_recycler_view)).check(matches(isDisplayed()));


    }

    @Test
    public void testGalleryIntent() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Apple"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("iPhone 12"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1000"));
        CameraAndGalleryTest.setDate(R.id.date_of_purchase_edit_text, 2023, 9, 15);
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("Blue"));
        Espresso.onView(withId(R.id.tag_edit_text)).perform(typeText("expensive, fast, durable"));

        onView(withId(R.id.upload_image_button)).perform(click());
        // Build an ActivityResult that will return from the camera app and set your extras (if any).
        Intent resultData = new Intent();
        resultData.putExtra(MediaStore.EXTRA_OUTPUT, "test.file.url");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Mock the gallery response. Now whenever an intent is sent to the camera, Espresso will respond with the result we pass here.
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

        // Now click on the button in your app that launches the camera.
        onView(withText("Gallery")).perform(click());


        // Check that the image is displayed
        Espresso.onView(withId(R.id.carousel_recycler_view)).check(matches(isDisplayed()));


    }
}



