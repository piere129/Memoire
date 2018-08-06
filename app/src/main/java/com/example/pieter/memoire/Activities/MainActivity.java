package com.example.pieter.memoire.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

       /* if (fm.findFragmentById(R.id.themesFragmentContainer) == null) {
            themesFragment = new ThemesFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.themesFragmentContainer, themesFragment);
            transaction.commit();
        }*/




        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            selectFragment("timeline");
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(),"test" + id,Toast.LENGTH_LONG).show();
            selectFragment("themes");

        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(),"test" + id,Toast.LENGTH_LONG).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectFragment(String fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(fragment) {
            case "themes":
                if(fragmentManager.findFragmentByTag("themes") != null) {
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("themes")).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.themesFragmentContainer, new ThemesFragment(), "themes").commit();
                }
                if(fragmentManager.findFragmentByTag("timeline") != null){
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("timeline")).commit();
                }
                break;

            case "timeline":
                if(fragmentManager.findFragmentByTag("timeline") != null) {
                    //if the fragment exists, show it.
                    fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("timeline")).commit();
                } else {
                    //if the fragment does not exist, add it to fragment manager.
                    fragmentManager.beginTransaction().add(R.id.timeline_fragment_container, new TimelineFragment(), "timeline").commit();
                }
                if(fragmentManager.findFragmentByTag("themes") != null){
                    //if the other fragment is visible, hide it.
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("themes")).commit();
                }
                break;
        }
    }

}
