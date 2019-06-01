package com.grvmishra788.remindtodo.basic;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ToDoItem {

    //constants
    public static final int CATEGORY_FINISHED = 0;
    public static final int CATEGORY_ONGOING = 1;
    public static final int CATEGORY_UPCOMING = 2;
    private static final String TAG = ToDoItem.class.getName();

    //ToDoItem variables
    private String mItemDescription;
    private Date mItemDate;
    private int mItemCategory;
    private UUID mItemID;

    //constructor
    public ToDoItem(String mItemDescription, Date mItemDate, int mItemCategory){
        Log.d(TAG, "ToDoItem Constructor starts");
        this.mItemDescription = mItemDescription;
        this.mItemDate = mItemDate;
        this.mItemCategory = mItemCategory;
        this.mItemID = UUID.randomUUID();
        Log.d(TAG, "ToDoItem Constructor ends");
    }

    //overloaded constructor 1
    public ToDoItem(String mItemDescription, Date mItemDate){
        //by default item is marked to be ONGOING
        this(mItemDescription, mItemDate, CATEGORY_ONGOING);
    }

    //overloaded constructor 2
    public ToDoItem(String mItemDescription){
        //by default item's date is set to be the EOD of ongoing day
        this(mItemDescription, new Date(Calendar.getInstance().YEAR,Calendar.getInstance().MONTH, Calendar.getInstance().DATE, 23, 59, 59 ), CATEGORY_ONGOING);
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
