package com.grvmishra788.remindtodo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.grvmishra788.remindtodo.MainFragment.FRAGMENT_CATEGORY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //contants
    private static final String TAG = MainActivity.class.getName();     //constant Class TAG

    //Variable To Store Associated Fragment
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate() called ");
        //set contentview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //set action bar
        setSupportActionBar(toolbar);

        //start initial fragment corresponding to ALL category
        setUpInitialFragment(savedInstanceState);

        //setup drawer toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d(TAG, "OnCreate() completed ");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed() called ");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Log.d(TAG, "OnBackPressed() completed ");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected() called ");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mFragment = new MainFragment();
        Bundle args = new Bundle();

        //set Fragment category as per menuitem selected
        if (id == R.id.nav_finished) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_finished);
        } else if (id == R.id.nav_overdue) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_overdue);
        } else if (id == R.id.nav_ongoing) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_ongoing);
        } else if (id == R.id.nav_upcoming) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_upcoming);
        } else if (id == R.id.nav_setting) {
            Toast.makeText(this, "Settings Activity!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "About Activity!", Toast.LENGTH_SHORT).show();
        }

        //associate bundle with fragment & launch it
        mFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,  mFragment)
                .commit();

        //close navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Log.d(TAG, "onNavigationItemSelected() completed ");
        return true;
    }

    private void setUpInitialFragment(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "setUpInitialFragment() called ");
        if (savedInstanceState == null) {
            mFragment = new MainFragment();
            Bundle args = new Bundle();
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_ongoing);
            mFragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .commit();
        }
        Log.d(TAG, "setUpInitialFragment() completed ");
    }

}

