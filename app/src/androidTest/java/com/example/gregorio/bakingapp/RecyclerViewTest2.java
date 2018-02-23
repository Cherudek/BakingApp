package com.example.gregorio.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Gregorio on 23/02/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecyclerViewTest2 {

  @Rule
  public ActivityTestRule<MainActivity> mMainActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void ingredientsRecyclerViewTest() {
    // First, scroll to the position that needs to be matched and click on it.
    onView(ViewMatchers.withId(R.id.rv_fragment_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(1,
            scrollTo()));
    // Match the text in an item below the fold and check that it's displayed.
    onView(withText("Brownies")).check(matches(isDisplayed()));

    onView(ViewMatchers.withId(R.id.rv_fragment_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(1,
            click()));

    onView(ViewMatchers.withId(R.id.btn_view_steps))
        .perform(click());

    onView(ViewMatchers.withId(R.id.rv_fragment_detail_list))
        .perform(RecyclerViewActions.actionOnItemAtPosition(2,
            ViewActions.scrollTo()));

    // Match the text in an item below the fold and check that it's displayed.
    onView(withText("2")).check(matches(isDisplayed()));

  }
}
