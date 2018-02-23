package com.example.gregorio.bakingapp;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }

  @Test
  public void mainActivityTest() {
    ViewInteraction textView = onView(
        allOf(withId(R.id.recipe_name), withText("Nutella Pie"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.rv_fragment_list),
                    0),
                0),
            isDisplayed()));
    textView.check(matches(withText("Nutella Pie")));

    ViewInteraction recyclerView = onView(
        allOf(withId(R.id.rv_fragment_list),
            childAtPosition(
                withClassName(is("android.widget.LinearLayout")),
                0)));
    recyclerView.perform(actionOnItemAtPosition(0, click()));

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.btn_view_steps), withText("View Steps"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.details_container),
                    0),
                2),
            isDisplayed()));
    appCompatButton.perform(click());

    ViewInteraction textView5 = onView(
        allOf(withId(R.id.step_id), withText("0"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.rv_fragment_step_list),
                    0),
                0),
            isDisplayed()));
    textView5.check(matches(withText("0")));

  }
}
