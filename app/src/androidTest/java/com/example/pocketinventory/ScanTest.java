package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;


import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.pocketinventory.Activities.HomePageActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ScanTest {

    private AutoSignInRule autoSignInRule = new AutoSignInRule();

    private ActivityScenarioRule<HomePageActivity> activityRule =
            new ActivityScenarioRule<>(HomePageActivity.class);

    @Rule
    public TestRule chain = RuleChain.outerRule(autoSignInRule).around(activityRule);
    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);


    @After
    @Before
    public void deleteItems() {
        ItemDB.getInstance().deleteAllItems();
    }

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    // Used source Stackoverflow for following code to simulate a click on a specific coordinate
    // Author : https://stackoverflow.com/users/103258/haffax
    // Date of publication : December 2nd, 2023
    // URL : https://stackoverflow.com/questions/22177590/click-by-bounds-coordinates
    // License : CC BY-SA 4.0. (https://creativecommons.org/licenses/by-sa/4.0/)
    public  ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    // fill forms to add an item
    private void fillInForms() {
        onView(withId(R.id.add_item)).perform(click());
        onView(ViewMatchers.withId(R.id.make_edit_text)).perform(ViewActions.replaceText("AAA"));
        onView(ViewMatchers.withId(R.id.model_edit_text)).perform(ViewActions.replaceText("AAA"));
        onView(ViewMatchers.withId(R.id.estimated_value_edit_text)).perform(ViewActions.replaceText("111"));
        onView(ViewMatchers.withId(R.id.description_edit_text)).perform(ViewActions.replaceText("AAA"));
        onView(ViewMatchers.withId(R.id.tag_edit_text)).perform(ViewActions.replaceText("BBB,AAA"));
        onView(ViewMatchers.withId(R.id.date_of_purchase_edit_text)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 10, 10));
        onView(withText("OK")).perform(click());
        onView(ViewMatchers.withId(R.id.serial_number_edit_text)).perform(ViewActions.replaceText("ABCD"));
        onView(withId(R.id.add_button)).perform(click());

    }


    // US 05.01.01 (Test whether the Scan Activity is displayed when the Scan drawable is clicked)
    @Test
    public void ScanActivitySwitchedTest() {

        fillInForms();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the first item in the list
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Click on the scan item drawble
        onView(isRoot()).perform(clickXY(1000, 400));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the scan activity is displayed
        onView(withId(R.id.scan_serial_number_button)).check(matches(isDisplayed()));

    }


    // US 05.01.01 (Test whether the Scan Activity working as expected)
    @Test
    public void ScanActivityTest() {

        fillInForms();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the first item in the list, then click on the scan item drawable
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(isRoot()).perform(clickXY(1000, 400));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mock the capture action to return a result to the Scan Activity
        // Ignore the warning message, as this is expected outcome for using Mock capture.
        // We only need to check whether we can proceed to the next step with the UI
        Intent resultData = new Intent();
        resultData.putExtra(MediaStore.EXTRA_OUTPUT, "test");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
        onView(withId(R.id.scan_serial_number_button)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check whether the serial number is successfully scanned and changed accordingly(It changes from "ABCD" to "" ).
        onView(withId(R.id.confirm_serial_number_button)).perform(click());
        onView(withId(R.id.serial_number_edit_text)).check(matches(withText("")));
    }
}


