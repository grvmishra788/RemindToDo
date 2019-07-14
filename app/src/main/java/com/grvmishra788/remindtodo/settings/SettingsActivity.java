package com.grvmishra788.remindtodo.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.grvmishra788.remindtodo.MainActivity;
import com.grvmishra788.remindtodo.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getName(); //constant Class TAG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate() called ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //init close activity button on left hand top side of activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_activity);

        //set title of activity
        setTitle("Settings");

        //replace content with SettingsFragment
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        Log.d(TAG, "OnCreate() completed ");
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp() called ");
        //call onBackPressed to finish activity and launch MainActivity
        onBackPressed();
        Log.d(TAG, "onSupportNavigateUp() completed ");
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() called ");
        //Restart Main Activity onBackPressed() to reflect changes
        startActivity(new Intent(this, MainActivity.class));
        //finish Current Activity
        finish();
        Log.d(TAG, "onBackPressed() completed ");
    }
}
