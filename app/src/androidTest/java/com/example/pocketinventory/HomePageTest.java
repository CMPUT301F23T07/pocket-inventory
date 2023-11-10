package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import android.view.View;
import android.widget.DatePicker;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.context.AttributeContext;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

/**
 * This class tests the home page of the app.
 *
 * Issues:
 * Currently the tests sometimes fail unless there is thread.sleep() between actions.
 * This is because the tests are running faster than the app can update the UI + database.
 * Need to find a better way to do this.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomePageTest {


    private AutoSignInRule autoSignInRule = new AutoSignInRule();

    private ActivityScenarioRule<HomePageActivity> activityRule =
            new ActivityScenarioRule<>(HomePageActivity.class);

    @Rule
    public TestRule chain = RuleChain.outerRule(autoSignInRule).around(activityRule);

    @After
    @Before
    public void deleteItems() {
        ItemDB.getInstance().deleteAllItems();
    }



    private void fillInFormSamsung() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Samsung"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("sPhone"));
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("some description here"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1234"));
        onView(withId(R.id.date_of_purchase_edit_text)).perform(click());
        // Set the date on the DatePicker widget
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 10, 10));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.comment_edit_text)).perform(ViewActions.typeText("my comment wbwbwbwbwbwb!!!"));
        onView(withId(R.id.tag_edit_text)).perform(ViewActions.typeText("mytag"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
    }

    private void fillInFormApple() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Apple"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("iPhone"));
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("some description here"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1234"));
        onView(withId(R.id.date_of_purchase_edit_text)).perform(click());
        // Set the date on the DatePicker widget
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 10, 10));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.comment_edit_text)).perform(ViewActions.typeText("my comment wbwbwbwbwbwb!!!"));
        onView(withId(R.id.tag_edit_text)).perform(ViewActions.typeText("mytag"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
    }

    // US 01.01.01

    // This also loosely tests the US 03.01.01 and US 03.02.01
    @Test
    public void testAddItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("sPhone")).check(matches(isDisplayed()));
    }

    // US 01.02.01
    @Test
    public void testViewItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.make_edit_text)).check(matches(withText("Samsung")));
        onView(withId(R.id.model_edit_text)).check(matches(withText("sPhone")));
        onView(withId(R.id.description_edit_text)).check(matches(withText("some description here")));
        onView(withId(R.id.estimated_value_edit_text)).check(matches(withText("1234.0")));
        onView(withId(R.id.comment_edit_text)).check(matches(withText("my comment wbwbwbwbwbwb!!!")));
        onView(withId(R.id.tag_edit_text)).check(matches(withText("mytag")));
        onView(withId(R.id.serial_number_edit_text)).check(matches(withText("1234567890")));
    }

    //US 01.03.01
    //As an owner, I want to edit the details of an item.
    @Test
    public void testEditItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // The fields are pre-filled with the original values. Change them.
        onView(withId(R.id.make_edit_text)).perform(replaceText("Samsung2"));
        onView(withId(R.id.model_edit_text)).perform(replaceText("sPhone2"));
        onView(withId(R.id.description_edit_text)).perform(replaceText("some description here2"));
        // save the changes
        onView(withId(R.id.add_button)).perform(click());
        // reopen the item
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verify that the fields are changed.
        onView(withId(R.id.make_edit_text)).check(matches(withText("Samsung2")));
        onView(withId(R.id.model_edit_text)).check(matches(withText("sPhone2")));
        onView(withId(R.id.description_edit_text)).check(matches(withText("some description here2")));
    }

    // US 01.04.01
    //As an owner, I want to delete an item.
    @Test
    public void testDeleteItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.delete_button)).perform(click());
        onView(withText("sPhone")).check(doesNotExist());
    }


    // US 02.07.01
    @Test
    public void testFilter() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.add_item)).perform(click());
        fillInFormApple();
        onView(withId(R.id.add_button)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.filterButton)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.makeInput)).perform(ViewActions.typeText("Apple"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withText("OK")).perform(click());
        onView(withText(containsString("iPhone"))).check(matches(isDisplayed()));
        onView(withText(containsString("sPhone"))).check(doesNotExist());
    }

    // US 02.03.01
    // As an owner, I want to select items from the list of items and delete the selected items
    @Test
    public void testSelectToDelete(){
        // Testing deleting one item:
        // Add an item
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform long press on the first item
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));


        // Delete the first (only) item
        onView(withId(R.id.delete_icon)).perform(click());

        // The item with model "sPhone" should not exist on the screen
        onView(withText(containsString("sPhone"))).check(doesNotExist());
        //__________________________________________________________________________________________
        // Test deleting all items:
        // Add two items
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.add_item)).perform(click());
        fillInFormApple();
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Long press on the first item
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));

        // Select all items
        onView(withId(R.id.select_all_icon)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Delete the selected items
        onView(withId(R.id.delete_icon)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the item list is empty
        onView(ViewMatchers.withId(R.id.log_list))
                .check(ViewAssertions.matches(hasChildCount(0)));

    }

    // US 03.03.01
    // As an owner, I want to select items from the list of items and apply one or more tags to the selected items.
    @Test
    public void testSelectToAddTags(){

        // Adding tags to one item:

        // Add an item
        onView(withId(R.id.add_item)).perform(click());
        fillInFormApple();
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Add a second item
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform a long press on the first item in the list
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));

        // Click add tags icon to the selected items
        onView(withId(R.id.add_tag_icon)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Add tags wanted to the tags EditText
        onView(withId(R.id.tagSelectedEditText)).perform(ViewActions.typeText("Good, Cleaned"));
        onView(withText("Confirm")).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the tags are added to the first item

        // Open the details of the first item (the item we just added tags to)
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The view should have the added tags
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText(containsString("Good"))));
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText(containsString("Cleaned"))));

        // Go back to home screen

        onView(withId(R.id.cancel_button)).perform(click());
        //________________________________________________________________________________________________________________

        // Adding tags to more than 1 one item, in this case all items:

        // Perform a long press on the first item in the list
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));

        // Click on select all button
        onView(withId(R.id.select_all_icon)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click add tags icon to the selected items
        onView(withId(R.id.add_tag_icon)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Add tags wanted to the tags EditText
        onView(withId(R.id.tagSelectedEditText)).perform(ViewActions.typeText("Durable, No Warranty"));
        onView(withText("Confirm")).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the tags are added to all the items

        // Open the details of the first item
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The view should have the added tags
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText(containsString("Durable"))));
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText(containsString("No Warranty"))));

        // Go back to home screen
        onView(withId(R.id.cancel_button)).perform(click());

        // Open the details of the second (last) item
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The view should have the added tags
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText(containsString("Durable"))));
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText(containsString("No Warranty"))));



    }

}
