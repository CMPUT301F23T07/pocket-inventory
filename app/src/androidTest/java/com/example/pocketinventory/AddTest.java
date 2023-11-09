package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pocketinventory.HomePageActivity;
import com.example.pocketinventory.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

    @RunWith(AndroidJUnit4.class)
    public class AddTest {

        @Rule
        public ActivityScenarioRule<HomePageActivity> activityScenarioRule = new ActivityScenarioRule<>(HomePageActivity.class);

        @Test
        public void testAddItemAndCheckModel() {
            // Click the "Add Item" button
            Espresso.onView(ViewMatchers.withId(R.id.add_item)).perform(ViewActions.click());

            // Fill in item details
            Espresso.onView(ViewMatchers.withId(R.id.make_edit_text)).perform(ViewActions.typeText("Apple"));
            Espresso.onView(ViewMatchers.withId(R.id.model_edit_text)).perform(ViewActions.typeText("iPhone"));
            Espresso.onView(ViewMatchers.withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1800"));
            Espresso.onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));

            Espresso.onView(ViewMatchers.withId(R.id.description_edit_text)).perform(ViewActions.typeText("xxxy"));
            Espresso.onView(ViewMatchers.withId(R.id.date_of_purchase_edit_text)).perform(ViewActions.click());


            Espresso.onView(withText("OK")).perform(click());
            // Choose a date here (you may need to select the date from the date picker)
            // Then, press "OK" to set the date.
            // You can use Espresso actions to interact with the date picker.

            Espresso.onView(ViewMatchers.withId(R.id.make_edit_text)).perform(ViewActions.typeText("s"));
            Espresso.onView(ViewMatchers.withId(R.id.add_button)).perform(ViewActions.click());

            // Check if the model matches "iPhone"
            onView(withText("iPhone")).perform(ViewActions.scrollTo()).check(matches(       isDisplayed()));
        }
    }


