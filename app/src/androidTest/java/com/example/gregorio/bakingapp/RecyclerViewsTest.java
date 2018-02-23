package com.example.gregorio.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.core.StringContains;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecyclerViewsTest {

  @Rule
  public ActivityTestRule<MainActivity> mMainActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void useAppContext() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();
    assertEquals("com.example.gregorio.bakingapp", appContext.getPackageName());
  }

  @Test
  public void recipeRecyclerViewTest() {

    // First, scroll to the position that needs to be matched and click on it.
    onView(ViewMatchers.withId(R.id.rv_fragment_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0,
            click()));

    // Match the text in an item below the fold and check that it's displayed.
    onView(withText("Nutella Pie")).check(matches(isDisplayed()));
  }
}



