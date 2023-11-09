package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
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
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
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
        onView(withText("Samsung")).check(matches(isDisplayed()));
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
    private void fillInAddItemWindow(String make, String model, String description, String estimatedValue, String comment, String tag, String serialNumber) {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText(make));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText(model));
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText(description));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText(estimatedValue));
        onView(withId(R.id.date_of_purchase_edit_text)).perform(click());
        onView(withText("OK")).perform(click()); // androidx.test.espresso.contrib.PickerActions can set date,
        // but I do not know how to import it
        onView(withId(R.id.comment_edit_text)).perform(ViewActions.typeText(comment));
        onView(withId(R.id.tag_edit_text)).perform(ViewActions.typeText(tag));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText(serialNumber));
    }
    @Test
    public void testSelectToDelete(){

        // Add an item
        onView(withId(R.id.add_item)).perform(click());
        fillInAddItemWindow("Samsung", "Galaxy", "Large black phone", "1000", "My favourite phone", "Black, Phone, Has warranty", "123345");
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("Galaxy")).check(matches(isDisplayed()));

        // Add a second item
        onView(withId(R.id.add_item)).perform(click());
        fillInAddItemWindow("Nike", "Air Jordon", "Fashionable white shoes", "200", "My favourite shoes", "White, Durable", "85466436");
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("Air Jordon")).check(matches(isDisplayed()));

        // Test deleting one item

        // Perform a long press on the item with text "Air Jordon" (model) in the RecyclerView
        onView(withText("Air Jordon")).perform(longClick());

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));

        // Delete the Nike item
        onView(withId(R.id.delete_icon)).perform(click());

        // The item with model "Air Jordon" should not exist on the screen
        onView(withText("Air Jordon")).check(doesNotExist());

        // Test deleting all items

        // Add the second item again
        onView(withId(R.id.add_item)).perform(click());
        fillInAddItemWindow("Nike", "Air Jordon", "Fashionable white shoes", "200", "My favourite shoes", "White, Durable", "85466436");
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("Air Jordon")).check(matches(isDisplayed()));

        // Perform a long press on the RecyclerView
        onView(withId(R.id.log_list)).perform(longClick());

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));

        // Click Select all
        onView(withId(R.id.select_all_icon)).perform(click());

        // Delete the selected items
        onView(withId(R.id.delete_icon)).perform(click());

        // Check if the recycler view containing the items is empty
        onView(ViewMatchers.withId(R.id.log_list))
                .check(ViewAssertions.matches(hasChildCount(0)));


    }

    @Test
    public void testSelectToAddTags(){

        // Add an item
        onView(withId(R.id.add_item)).perform(click());
        fillInAddItemWindow("Samsung", "Galaxy", "Large black phone", "1000", "My favourite phone", "Black, Phone, Has warranty", "123345");
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("Galaxy")).check(matches(isDisplayed()));

        // Add a second item
        onView(withId(R.id.add_item)).perform(click());
        fillInAddItemWindow("Nike", "Air Jordon", "Fashionable white shoes", "200", "My favourite shoes", "White, Durable", "85466436");
        onView(withId(R.id.add_button)).perform(click());
        onView(withText("Air Jordon")).check(matches(isDisplayed()));

        // Perform a long press on the item with model "Air Jordon" in the RecyclerView
        onView(withText("Air Jordon")).perform(longClick());

        // Check if necessary selection icons appear
        onView(withId(R.id.delete_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.add_tag_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all_icon)).check(matches(isDisplayed()));

        // Click add tags icon to the selected items
        onView(withId(R.id.add_tag_icon)).perform(click());

        // Add tags wanted to the tags EditText
        onView(withId(R.id.tagSelectedEditText)).perform(ViewActions.typeText("Has Warranty, Cleaned"));
        onView(withText("Confirm")).perform(click());

        // Check if the tags are added to the item with Model "Air Jordon"

        // Open the details of the item with "Air Jordon" as the Make
        onView(withText("Air Jordon")).perform(click());

        // The view should have the added tags
        onView(withId(R.id.tag_edit_text)).check(ViewAssertions.matches(withText("Has Warranty, Cleaned")));

    }

}
