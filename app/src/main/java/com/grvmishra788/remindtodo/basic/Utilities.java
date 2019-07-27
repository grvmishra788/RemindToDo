package com.grvmishra788.remindtodo.basic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grvmishra788.remindtodo.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//Utilities class added to perform general functions from anywhere in the code
public class Utilities {

    //constant Class TAG
    private static final String TAG = Utilities.class.getName();

    //function to save string To SharedPrefences
    public static void saveStringToSharedPreferences(SharedPreferences mSharedPreferences, String strType, String str) {
        Log.d(TAG, "Started saving string to Shared Preferences for type - " + strType);
        SharedPreferences.Editor mSharedPreferencesEditor = mSharedPreferences.edit();
        mSharedPreferencesEditor.putString(strType, str);
        mSharedPreferencesEditor.apply();
        Log.d(TAG, "Completed saving string to Shared Preferences for type - " + strType);
    }

    //function to load string From SharedPrefences
    public static String loadStringFromSharedPreferences(SharedPreferences mSharedPreferences, String strType) {
        Log.d(TAG, "Started loading string from Shared Preferences for type - " + strType);
        String str = mSharedPreferences.getString(strType, null);
        Log.d(TAG, "Completed loading string to Shared Preferences for type - " + strType);
        return str;
    }

    //function to save ToDoList To SharedPrefences
    public static void saveToDoListToSharedPreferences(SharedPreferences mSharedPreferences, ArrayList<ToDoItem> mToDoItems) {
        Log.d(TAG, "Started saving ToDoList to Shared Preferences");
        Gson mGson = new Gson();
        String json = mGson.toJson(mToDoItems);
        saveStringToSharedPreferences(mSharedPreferences, "ToDoList", json);
        Log.d(TAG, "Ended saving ToDoList to Shared Preferences");
    }

    //function to load ToDoList From SharedPrefences
    public static ArrayList<ToDoItem> loadToDoListFromSharedPreferences(SharedPreferences mSharedPreferences) {
        Log.d(TAG, "Started loading ToDoList from Shared Preferences");
        Gson mGson = new Gson();
        String json = loadStringFromSharedPreferences(mSharedPreferences, "ToDoList");
        Type mType = new TypeToken<ArrayList<ToDoItem>>() {
        }.getType();
        ArrayList<ToDoItem> mToDoItem = mGson.fromJson(json, mType);
        Log.d(TAG, "Ended loading ToDoList from Shared Preferences");
        return mToDoItem;
    }

    //utility function to return EOD
    public static Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    //utility function to return start of current day
    public static Date getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }

    //utility function to return start of next day
    public static Date getStartOfNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    //utility function to init textView with required settings
    public static void initTextView(TextView mTextView, int gravity, int typeFace, int color, String text){
        mTextView.setGravity(gravity);
        mTextView.setTypeface(null, typeFace);
        mTextView.setTextColor(color);
        mTextView.setText(text);
    }

}
