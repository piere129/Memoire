package com.example.pieter.memoire;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.pieter.memoire.Activities.MainActivity;
import com.example.pieter.memoire.Fragments.ThemesFragment;
import com.example.pieter.memoire.Fragments.TimelineFragment;
import com.example.pieter.memoire.Models.Theme;
import com.example.pieter.memoire.Persistence.ThemeDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GridClickTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public void intent() {
        Intent intent = new Intent();
        intent.putExtra("your_key", "your_value");

        mActivityRule.launchActivity(intent);
    }


    @Test
    public void checkThemeFragmentDisplayedOnAddingFragmentContainer() {
        ThemesFragment fragment = new ThemesFragment();

        Espresso.onView(withId(R.id.timeline_fragment_container)).check(matches((isDisplayed())));
    }

    @Test
    public void checkThemeRecyclerViewDisplayed() {
        ThemesFragment fragment = new ThemesFragment();

        Espresso.onView(withTagValue(is((Object)"theme_recyclerview"))).check(matches((isDisplayed())));
    }

    @Test
    public void checkTimelineRecyclerViewOnClickItemAtPosition0ShowsDialogWithDescription() {
        TimelineFragment fragment = new TimelineFragment();
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.timeline_fragment_container, fragment).commit();

        Espresso.onView(withTagValue(is((Object)"timeline_recyclerview"))).check(matches((isDisplayed())));

        Espresso.onView(withTagValue(is((Object)"timeline_recyclerview")))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        Espresso.onView(withText("Description")).check(matches(isDisplayed()));
    }

    @Test
    public void checkThemeRecyclerViewListFromDatabaseMatchesPositionRecyclerview() {
        ThemesFragment fragment = new ThemesFragment();

        List<Theme> themes = ThemeDatabase.getInstance(mActivityRule.getActivity()).getThemeDao().getThemes();
        Theme theme = themes.get(0);

        Espresso.onView(withText(theme.getName())).check(matches(isDisplayed()));
    }

    @Test
    public void checkThemeRecyclerViewOnLongClickDeleteTheme() {
        ThemesFragment fragment = new ThemesFragment();

        Espresso.onView(withTagValue(is((Object)"theme_recyclerview"))).check(matches((isDisplayed())));
        List<Theme> themes = ThemeDatabase.getInstance(mActivityRule.getActivity()).getThemeDao().getThemes();
        Theme theme = themes.get(0);
         Espresso.onView(withTagValue(is((Object)"theme_recyclerview")))
               .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.longClick()));

       Espresso.onView(withText(theme.getName())).check(doesNotExist());
    }



}
