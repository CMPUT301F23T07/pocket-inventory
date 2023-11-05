package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;
import android.widget.DatePicker;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomePageTest {
    @Rule public ActivityScenarioRule<HomePageActivity> activityRule
            = new ActivityScenarioRule<>(HomePageActivity.class);

    private void fillInForm() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Samsung"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("sPhone"));
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("some description here"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1234"));
        onView(withId(R.id.date_of_purchase_edit_text)).perform(click());
        onView(withText("OK")).perform(click()); // androidx.test.espresso.contrib.PickerActions can set date,
        // but I do not know how to import it
        onView(withId(R.id.comment_edit_text)).perform(ViewActions.typeText("my comment wbwbwbwbwbwb!!!"));
        onView(withId(R.id.tag_edit_text)).perform(ViewActions.typeText("mytag"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
    }
    @Test
    public void testAddItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInForm();
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("samsung")).check(matches(isDisplayed()));
    }

    @Test
    public void testFilter() {
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.makeInput)).perform(ViewActions.typeText("apple"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withText("OK")).perform(click());
        onView(withText("apple")).check(matches(isDisplayed()));
        onView(withText("Asus")).check(doesNotExist());
    }

}
