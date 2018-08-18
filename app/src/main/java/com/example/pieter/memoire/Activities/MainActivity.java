package com.example.pieter.memoire.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pieter.memoire.Fragments.ThemesFragment;
import com.example.pieter.memoire.Fragments.TimelineFragment;
import com.example.pieter.memoire.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    FragmentManager fragmentManager;


    /**
     * Initialises the Mainactivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentByTag("timeline") == null && fragmentManager.findFragmentByTag("timeline") == null)
        {
            fragmentManager.beginTransaction().add(R.id.themesFragmentContainer, new ThemesFragment(), "themes").commit();

        }
    }

    /**
     * Opens and closes the drawer
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Allows switching between fragments onClick of a
     * Drawer item.
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            selectFragment("timeline");
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "test" + id, Toast.LENGTH_LONG).show();
            selectFragment("themes");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Replaces the current Fragment with
     * a new fragment when choosing an item from the drawer.
     *
     * @param fragment
     */
    private void selectFragment(String fragment) {
        switch (fragment) {
            case "themes":
                if (fragmentManager.findFragmentByTag("timeline") != null) {
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("timeline")).commit();
                }
                fragmentManager.beginTransaction().replace(R.id.themesFragmentContainer, new ThemesFragment(), "themes").commit();
                break;

            case "timeline":

                if (fragmentManager.findFragmentByTag("themes") != null) {
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("themes")).commit();
                }

                fragmentManager.beginTransaction().replace(R.id.timeline_fragment_container, new TimelineFragment(), "timeline").commit();

                break;
        }
    }

}
