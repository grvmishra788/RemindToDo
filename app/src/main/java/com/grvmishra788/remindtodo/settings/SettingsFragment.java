package com.grvmishra788.remindtodo.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.grvmishra788.remindtodo.R;

public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = SettingsFragment.class.getName();     //constant Class TAG

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate() called ");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //init list preferences
        initListPreference("pref_listType");
        initListPreference("pref_sortType");

        //init CheckBox Preferences
        initCheckBoxPreference("pref_24hourView");
        initCheckBoxPreference("pref_confirmFinishing");
        initCheckBoxPreference("pref_confirmDeleting");

        Log.d(TAG, "OnCreate() completed ");
    }

    private void initCheckBoxPreference(String type){
        Log.d(TAG, "initCheckBoxPreference() called for type - "+type);
        //init CheckBoxPreference variable
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(type);

        //set summary based on whether checkbox is enabled/disabled
        if(checkBoxPreference.isChecked()){
            checkBoxPreference.setSummary("Enabled");
        } else {
            checkBoxPreference.setSummary("Disabled");
        }

        //set its OnPreferenceChangeListener
        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                CheckBoxPreference checkBoxPreference1 = (CheckBoxPreference) preference;
                if(checkBoxPreference1.isChecked()){
                    //if earlier preference was checked, now it will be disabled
                    preference.setSummary("Disabled");
                } else {
                    //else, now it will be enabled
                    preference.setSummary("Enabled");
                }
                return true;
            }
        });

        Log.d(TAG, "initCheckBoxPreference() completed for type - "+type);
    }

    private void initListPreference(String type){
        //insert values in ListPreference summary & set OnPreferenceChangeListener
        Log.d(TAG, "initListPreference() called for type - "+type);
        ListPreference listPreference = (ListPreference) findPreference(type);
        if(listPreference.getValue()==null) {
            // to ensure we don't get a null value
            // set first value by default
            listPreference.setValueIndex(0);
        }
        int index = listPreference.findIndexOfValue(listPreference.getValue());
        if(index>=0)
            listPreference.setSummary(listPreference.getEntries()[index]);
        listPreference.setOnPreferenceChangeListener(mOnPreferenceChangeListener);
        Log.d(TAG, "initListPreference() completed for type - "+type);
    }

    Preference.OnPreferenceChangeListener mOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            ListPreference listPreference = (ListPreference)preference;
            int id = 0;
            for (int i = 0; i < listPreference.getEntryValues().length; i++)
            {
                if(listPreference.getEntryValues()[i].equals(newValue.toString())){
                    id = i;
                    break;
                }
            }
            preference.setSummary(listPreference.getEntries()[id]);
            return true;
        }
    };

}
