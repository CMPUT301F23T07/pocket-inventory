package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.TypeTextAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

@RunWith(AndroidJUnit4.class)
public class ScanTest {

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
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


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
        onView(withId(R.id.log_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }
}


