package com.grvmishra788.remindtodo.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.grvmishra788.remindtodo.MainFragment;
import com.grvmishra788.remindtodo.R;

public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = SettingsFragment.class.getName();     //constant Class TAG

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate() called ");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Log.d(TAG, "OnCreate() completed ");
    }
}
