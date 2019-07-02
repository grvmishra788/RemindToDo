package com.grvmishra788.remindtodo.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.grvmishra788.remindtodo.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = AboutActivity.class.getName(); //constant Class TAG
    @Nullable
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called for " + TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //init close activity button on left hand top side of activity
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_activity);

        //set title of activity
        setTitle("About");
    }
}
