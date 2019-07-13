package com.grvmishra788.remindtodo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.grvmishra788.remindtodo.about.AboutActivity;
import com.grvmishra788.remindtodo.basic.Utilities;
import com.grvmishra788.remindtodo.settings.SettingsActivity;

import static com.grvmishra788.remindtodo.MainFragment.COMPARATOR_TYPE;
import static com.grvmishra788.remindtodo.MainFragment.FRAGMENT_CATEGORY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //contants
    private static final String TAG = MainActivity.class.getName();     //constant Class TAG

    //Variable To Store Associated Fragment
    private Fragment mFragment;

    //Variable to store User Settings SharedPreference
    private SharedPreferences userPreferences;

    //Variable to store defaultFragment Type Value i.e. one out of {-1,0,1,2,3}
    private int defaultFragmentValue;

    //Variable to store default Comparator Type
    // 0 -> Alphabetical, 1-> Due Date
    private int defaultComparatorType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate() called ");
        //set contentview
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //set action bar
        setSupportActionBar(toolbar);

        //--------------------init user settings----------------------
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // 0 -> ongoing should be the selected fragment type if user hasn't selected any
        // 0 -> alphabetical sorting should be the selected defaultComparatorType if user hasn't selected any
        if (userPreferences != null) {
            // get defaultFragmentValue & defaultComparatorType from default Shared Preference
            String defaultFragment = userPreferences.getString("pref_listType", "0");
            defaultFragmentValue = (defaultFragment == null) ? 0 : Integer.parseInt(defaultFragment);
            String defaultComparator = userPreferences.getString("pref_sortType", "0");
            defaultComparatorType = (defaultComparator == null) ? 0 : Integer.parseInt(defaultComparator);
        } else {
            defaultFragmentValue = 0;
            defaultComparatorType = 0;
        }

        //sort ToDoItems alphabetically by default
        //-------------------------------------------------------------

        //start initial fragment corresponding to ALL category
        setUpInitialFragment(savedInstanceState, defaultFragmentValue);

        //setup drawer toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(defaultFragmentValue + 1).setChecked(true);
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

        //create bundle to store fragment category
        Bundle args = new Bundle();
        args.putInt(COMPARATOR_TYPE, defaultComparatorType);

        //set Fragment category as per menuitem selected
        if (id == R.id.nav_finished) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_finished);
        } else if (id == R.id.nav_overdue) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_overdue);
        } else if (id == R.id.nav_ongoing) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_ongoing);
        } else if (id == R.id.nav_upcoming) {
            args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_upcoming);
        }

        //create fragment
        if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            mFragment = new MainFragment();
            //associate bundle with fragment
            mFragment.setArguments(args);
            //launch the fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .commit();
        }

        //close navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Log.d(TAG, "onNavigationItemSelected() completed ");
        return true;
    }

    private void setUpInitialFragment(@Nullable Bundle savedInstanceState, int defaultFragmentValue) {
        Log.d(TAG, "setUpInitialFragment() called ");
        if (savedInstanceState == null) {
            mFragment = new MainFragment();
            Bundle args = new Bundle();

            //Conversion from values present in "pref_toDoList_values" array to corresponding Fragment type
            if (defaultFragmentValue == 0) {
                args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_ongoing);
            } else if (defaultFragmentValue == 1) {
                args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_upcoming);
            } else if (defaultFragmentValue == 2) {
                args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_finished);
            } else if (defaultFragmentValue == 3) {
                args.putInt(FRAGMENT_CATEGORY, R.drawable.ic_overdue);
            }

            mFragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .commit();
        }
        Log.d(TAG, "setUpInitialFragment() completed ");
    }

}

