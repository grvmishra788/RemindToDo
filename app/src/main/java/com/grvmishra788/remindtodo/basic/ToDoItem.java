package com.grvmishra788.remindtodo.basic;

import android.util.Log;

import com.grvmishra788.remindtodo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ToDoItem {

    //constants
    private static final String TAG = ToDoItem.class.getName();

    //ToDoItem variables
    private String mItemDescription;
    private Date mItemDate;
    private int mItemCategory;
    private UUID mItemID;

    //constructor
    public ToDoItem(String mItemDescription, Date mItemDate, int mItemCategory){
        Log.d(TAG, TAG + ": Constructor starts");
        this.mItemDescription = mItemDescription;
        this.mItemDate = mItemDate;
        this.mItemCategory = mItemCategory;
        this.mItemID = UUID.randomUUID();
        Log.d(TAG, TAG + ": Constructor ends");
    }

    //overloaded constructor 1
    public ToDoItem(String mItemDescription, Date mItemDate){
        //by default item is marked to be ONGOING
        this(mItemDescription, mItemDate, R.drawable.ic_ongoing);
    }

    //overloaded constructor 2
    public ToDoItem(String mItemDescription){
        //by default item's date is set to be the EOD of ongoing day
        this(mItemDescription, new Date(Calendar.getInstance().YEAR,Calendar.getInstance().MONTH, Calendar.getInstance().DATE, 23, 59, 59 ), R.drawable.ic_ongoing);
    }

    //accessor methods
    public Date getmItemDate() {
        return mItemDate;
    }

    public int getmItemCategory() {
        return mItemCategory;
    }

    public String getmItemDescription() {
        return mItemDescription;
    }

    public UUID getmItemID() {
        return mItemID;
    }

    //mutator methods
    public void setmItemCategory(int mItemCategory) {
        this.mItemCategory = mItemCategory;
    }

    public void setmItemDate(Date mItemDate) {
        this.mItemDate = mItemDate;
    }

    public void setmItemDescription(String mItemDescription) {
        this.mItemDescription = mItemDescription;
    }

    public void setmItemID(UUID mItemID) {
        this.mItemID = mItemID;
    }
}
