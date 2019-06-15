package com.grvmishra788.remindtodo.basic;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//Utilities class added to perform general functions from anywhere in the code
public class Utilities {

    //constant Class TAG
    private static final String TAG = Utilities.class.getName();

    //function to save ToDoList To SharedPrefences
    public static void saveToDoListToSharedPreferences(SharedPreferences mSharedPreferences, ArrayList<ToDoItem> mToDoItems){
        Log.d(TAG, "Started saving ToDoList to Shared Preferences");
        SharedPreferences.Editor mSharedPreferencesEditor = mSharedPreferences.edit();
        Gson mGson = new Gson();
        String json = mGson.toJson(mToDoItems);
        mSharedPreferencesEditor.putString("ToDoList", json);
        mSharedPreferencesEditor.apply();
        Log.d(TAG, "Ended saving ToDoList to Shared Preferences");
    }

    //function to load ToDoList From SharedPrefences
    public static ArrayList<ToDoItem> loadToDoListFromSharedPreferences(SharedPreferences mSharedPreferences){
        Log.d(TAG, "Started loading ToDoList from Shared Preferences");
        Gson mGson = new Gson();
        String json = mSharedPreferences.getString("ToDoList", null);
        Type mType = new TypeToken<ArrayList<ToDoItem>>(){}.getType();
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

}
