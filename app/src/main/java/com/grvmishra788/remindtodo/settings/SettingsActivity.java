package com.grvmishra788.remindtodo.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.grvmishra788.remindtodo.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getName(); //constant Class TAG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //init close activity button on left hand top side of activity
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_activity);

        //set title of activity
        setTitle("Settings");

    }
}
