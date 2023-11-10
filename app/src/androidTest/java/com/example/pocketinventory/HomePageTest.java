package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
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


    private TestRule autoSignInRule = (TestRule) new AutoSignInRule();

    private ActivityScenarioRule<HomePageActivity> activityRule =
            new ActivityScenarioRule<>(HomePageActivity.class);

    @Rule
    public TestRule chain = RuleChain.outerRule(autoSignInRule).around(activityRule);

    @After
    @Before
    public void deleteItems() {
        ItemDB.getInstance().deleteAllItems();
    }

    //use onView(isRoot()).perform(waitFor(5000)) to wait for 5 seconds
    public static ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override public String getDescription() {
                return "wait for " + delay + "milliseconds";
            }

            @Override public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }

    private void fillInFormSamsung() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Samsung"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("sPhone"));
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("non-iphone description"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1234"));
        onView(withId(R.id.date_of_purchase_edit_text)).perform(click());
        // Set the date on the DatePicker widget
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.comment_edit_text)).perform(ViewActions.typeText("my comment wbwbwbwbwbwb!!!"));
        onView(withId(R.id.tag_edit_text)).perform(ViewActions.typeText("mytag1"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
    }

    private void fillInFormApple() {
        onView(withId(R.id.make_edit_text)).perform(ViewActions.typeText("Apple"));
        onView(withId(R.id.model_edit_text)).perform(ViewActions.typeText("iPhone"));
        onView(withId(R.id.description_edit_text)).perform(ViewActions.typeText("iphone description"));
        onView(withId(R.id.estimated_value_edit_text)).perform(ViewActions.typeText("1234"));
        onView(withId(R.id.date_of_purchase_edit_text)).perform(click());
        // Set the date on the DatePicker widget
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2023, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.comment_edit_text)).perform(ViewActions.typeText("my comment wbwbwbwbwbwb!!!"));
        onView(withId(R.id.tag_edit_text)).perform(ViewActions.typeText("mytag2"));
        onView(withId(R.id.serial_number_edit_text)).perform(ViewActions.typeText("1234567890"));
    }

    // US 01.01.01
    @Test
    public void testAddItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
    }

    // US 01.02.01
    @Test
    public void testViewItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.make_edit_text)).check(matches(withText("Samsung")));
        onView(withId(R.id.model_edit_text)).check(matches(withText("sPhone")));
        onView(withId(R.id.description_edit_text)).check(matches(withText("non-iphone description")));
        onView(withId(R.id.estimated_value_edit_text)).check(matches(withText("1234.0")));
        onView(withId(R.id.comment_edit_text)).check(matches(withText("my comment wbwbwbwbwbwb!!!")));
        onView(withId(R.id.tag_edit_text)).check(matches(withText("mytag1")));
        onView(withId(R.id.serial_number_edit_text)).check(matches(withText("1234567890")));
    }

    //US 01.03.01
    //As an owner, I want to edit the details of an item.
    @Test
    public void testEditItem() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
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
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.log_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.delete_button)).perform(click());
        onView(withText("sPhone")).check(doesNotExist());
    }


    // US 02.06.01 and US 02.07.01
    @Test
    public void testFilter() {
        //Create two items
        //Combine all filter test here to save time
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.add_item)).perform(click());
        fillInFormApple();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        //filter by make
        onView(withId(R.id.filterButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.makeInput)).perform(ViewActions.typeText("Apple"));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(matches(isDisplayed()));
        onView(withText(containsString("sPhone"))).check(doesNotExist());
        //filter by description
        onView(withId(R.id.filterButton)).perform(click()).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.descriptionInput)).perform(ViewActions.typeText("non"));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(doesNotExist());
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
    }

    // US 02.05.01
    @Test
    public void testFilterDate() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.add_item)).perform(click());
        fillInFormApple();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        //filter by date, bounded blow
        onView(withId(R.id.filterButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonAfter)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 10, 10));
        onView(isRoot()).perform(waitFor(50));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("sPhone"))).check(doesNotExist());
        onView(withText(containsString("iPhone"))).check(matches(isDisplayed()));
        //bounded above
        onView(withId(R.id.filterButton)).perform(click()).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonBefore)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(doesNotExist());
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
        //bounded both, but include nothing
        onView(withId(R.id.filterButton)).perform(click()).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonBefore)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.buttonAfter)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(doesNotExist());
        onView(withText(containsString("sPhone"))).check(doesNotExist());
        //bounded both, include one
        onView(withId(R.id.filterButton)).perform(click()).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonAfter)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.buttonBefore)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(doesNotExist());
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
        //inversely bounded
        onView(withId(R.id.filterButton)).perform(click()).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonAfter)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.buttonBefore)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 10, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(doesNotExist());
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
    }

    //US 03.05.01
    @Test
    public void testFilterTag() {
        onView(withId(R.id.add_item)).perform(click());
        fillInFormSamsung();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.add_item)).perform(click());
        fillInFormApple();
        onView(withId(R.id.add_button)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        //filter by tag, match All
        onView(withId(R.id.filterButton)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonAddTag)).perform(click());
        onView(withId(R.id.filterTagAutoComplete)).perform(ViewActions.typeText("mytag1"));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(doesNotExist());
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
        //match one
        onView(withId(R.id.filterButton)).perform(click()).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withId(R.id.buttonAddTag)).perform(click());
        onView(withId(R.id.filterTagAutoComplete)).perform(ViewActions.typeText("mytag1"));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.buttonAddTag)).perform(click());
        onView(withId(R.id.filterTagAutoComplete)).perform(ViewActions.typeText("mytag2"));
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.buttonOR)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(isRoot()).perform(waitFor(500));
        onView(withText(containsString("iPhone"))).check(matches(isDisplayed()));
        onView(withText(containsString("sPhone"))).check(matches(isDisplayed()));
    }

}
