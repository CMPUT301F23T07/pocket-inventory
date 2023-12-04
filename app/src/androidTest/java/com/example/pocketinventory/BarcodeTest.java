package com.example.pocketinventory;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.rule.GrantPermissionRule;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;



import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.view.MotionEvent;
import android.view.View;

import android.widget.TextView;

import androidx.test.espresso.matcher.BoundedMatcher;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;



import android.graphics.Point;

import com.google.zxing.client.android.Intents;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import org.junit.runner.RunWith;


/**
 * This class tests the barcode scanning.

 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BarcodeTest {


    private TestRule autoSignInRule = (TestRule) new AutoSignInRule();
    private ActivityScenarioRule<HomePageActivity> activityRule =
            new ActivityScenarioRule<>(HomePageActivity.class);

    @Rule
    public TestRule chain = RuleChain.outerRule(autoSignInRule).around(activityRule);

    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @Before
    public void setUp() {
        androidx.test.espresso.intent.Intents.init();
    }
    @After
    public void tearDown() {
        androidx.test.espresso.intent.Intents.release();
    }

    /**
         * This method help click on the drawable of a textview.
         * Reference: https://stackoverflow.com/a/57052764
         */
    public static ViewAction clickDrawables() {
        return new ViewAction()
        {
            @Override
            public Matcher<View> getConstraints()//must be a textview with drawables to do perform
            {
                return allOf(isAssignableFrom(TextView.class), new BoundedMatcher<View, TextView>(TextView.class)
                {
                    @Override
                    protected boolean matchesSafely(final TextView tv)
                    {
                        if(tv.requestFocusFromTouch())//get fpocus so drawables become visible
                            for(Drawable d : tv.getCompoundDrawables())//if the textview has drawables then return a match
                                if(d != null)
                                    return true;

                        return false;
                    }

                    @Override
                    public void describeTo(org.hamcrest.Description description)
                    {
                        description.appendText("has drawable");
                    }
                });
            }

            @Override
            public String getDescription()
            {
                return "click drawables";
            }

            @Override
            public void perform(final UiController uiController, final View view)
            {
                TextView tv = (TextView)view;
                if(tv != null && tv.requestFocusFromTouch())//get focus so drawables are visible
                {
                    Drawable[] drawables = tv.getCompoundDrawables();

                    Rect tvLocation = new Rect();
                    tv.getHitRect(tvLocation);

                    Point[] tvBounds = new Point[4];//find textview bound locations
                    tvBounds[0] = new Point(tvLocation.left, tvLocation.centerY());
                    tvBounds[1] = new Point(tvLocation.centerX(), tvLocation.top);
                    tvBounds[2] = new Point(tvLocation.right, tvLocation.centerY());
                    tvBounds[3] = new Point(tvLocation.centerX(), tvLocation.bottom);

                    for(int location = 0; location < 4; location++)
                        if(drawables[location] != null)
                        {
                            Rect bounds = drawables[location].getBounds();
                            tvBounds[location].offset(bounds.width() / 2, bounds.height() / 2);//get drawable click location for left, top, right, bottom
                            if(tv.dispatchTouchEvent(MotionEvent.obtain(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, tvBounds[location].x, tvBounds[location].y, 0)))
                                tv.dispatchTouchEvent(MotionEvent.obtain(android.os.SystemClock.uptimeMillis(), android.os.SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, tvBounds[location].x, tvBounds[location].y, 0));
                        }
                }
            }
        };
    }

    /**
     * This method tests the barcode scanning, by faking a scan result.
     */
    @Test
    public void test1() {
        //Set up fake result
        Intent resultData = new Intent();
        resultData.putExtra("SCAN_RESULT", "123123321");
        //click
        intending(hasAction(Intents.Scan.ACTION)).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));
        onView(withId(R.id.add_item)).perform(click());
        onView(withId(R.id.description_edit_text)).perform(clickDrawables());
        intended(hasAction(Intents.Scan.ACTION));
        onView(withId(R.id.description_edit_text)).check(matches(withText("123123321")));
    }

}
