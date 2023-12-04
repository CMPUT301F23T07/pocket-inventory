package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import android.widget.DatePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
public class SortTest {

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
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.add_item)).perform(click());
        onView(ViewMatchers.withId(R.id.make_edit_text)).perform(ViewActions.replaceText("BBB"));
        onView(ViewMatchers.withId(R.id.model_edit_text)).perform(ViewActions.replaceText("BBB"));
        onView(ViewMatchers.withId(R.id.estimated_value_edit_text)).perform(ViewActions.replaceText("222"));
        onView(ViewMatchers.withId(R.id.description_edit_text)).perform(ViewActions.replaceText("BBB"));
        onView(ViewMatchers.withId(R.id.tag_edit_text)).perform(ViewActions.replaceText("CCC,BBB"));
        onView(ViewMatchers.withId(R.id.date_of_purchase_edit_text)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 10, 11));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_button)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.add_item)).perform(click());
        onView(ViewMatchers.withId(R.id.make_edit_text)).perform(ViewActions.replaceText("CCC"));
        onView(ViewMatchers.withId(R.id.model_edit_text)).perform(ViewActions.replaceText("CCC"));
        onView(ViewMatchers.withId(R.id.estimated_value_edit_text)).perform(ViewActions.replaceText("333"));
        onView(ViewMatchers.withId(R.id.description_edit_text)).perform(ViewActions.replaceText("CCC"));
        onView(ViewMatchers.withId(R.id.date_of_purchase_edit_text)).perform(ViewActions.click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 10, 12));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_button)).perform(click());

    }


    // US 02.04.01 (Test whether the Sorter Fragment is successfully displayed)
    @Test
    public void SortFragmentSwitchedTest() {
        onView(withId(R.id.sorterButton)).perform(click());
        onView(withId(R.id.Sortby)).check(matches(isDisplayed()));
    }

    // US 02.04.01 and US 02.05.01 (Test whether the Sorting functionality is working as intended)
    // US 02.04.01 (Sort By Date)
    @Test
    public void SortTestDate(){
        fillInForms();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.Date_btn_des)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.model_edit_text)).check(matches(withText("CCC")));
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.Date_btn_asc)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.model_edit_text)).check(matches(withText("AAA")));
        onView(withId(R.id.cancel_button)).perform(click());

    }

    // US 02.04.01 (Sort By Description)
    @Test
    public void SortTestDescription(){
        fillInForms();
        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.Description_btn_des)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.description_edit_text)).check(matches(withText("CCC")));
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.Description_btn_asc)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.description_edit_text)).check(matches(withText("AAA")));
        onView(withId(R.id.cancel_button)).perform(click());

    }

    // US 02.04.01 (Sort By Make)
    @Test
    public void SortTestMake(){
        fillInForms();
        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.Make_btn_des)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.make_edit_text)).check(matches(withText("CCC")));
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.Make_btn_asc)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.make_edit_text)).check(matches(withText("AAA")));
        onView(withId(R.id.cancel_button)).perform(click());

    }

    // US 02.04.01 (Sort By Value)
    @Test
    public void SortTestValue(){
        fillInForms();
        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.Value_btn_des)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.estimated_value_edit_text)).check(matches(withText("333.0")));
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.Value_btn_asc)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.estimated_value_edit_text)).check(matches(withText("111.0")));
        onView(withId(R.id.cancel_button)).perform(click());

    }

    // US 02.05.01 (Sort By Tag)
    @Test
    public void SortTestTag(){
        fillInForms();
        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.Tag_btn_des)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tag_edit_text)).check(matches(withText("BBB, CCC")));
        onView(withId(R.id.cancel_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        //onView(withId(R.id.tag_edit_text)).check(matches(withText("")));
        onView(withId(R.id.cancel_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.sorterButton)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.Tag_btn_asc)).perform(click());
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tag_edit_text)).check(matches(withText("AAA, BBB")));
        onView(withId(R.id.cancel_button)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        //onView(withId(R.id.tag_edit_text)).check(matches(withText("")));
        onView(withId(R.id.cancel_button)).perform(click());


    }

}
