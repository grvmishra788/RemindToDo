package com.grvmishra788.remindtodo.basic;

import android.util.Log;

import com.grvmishra788.remindtodo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ToDoItem {

    //constant Class TAG
    private static final String TAG = ToDoItem.class.getName();

    //ToDoItem variables
    private String mItemDescription;
    private Date mItemDate;
    private int mItemCategory;
    private UUID mItemID;

    //constructor
    public ToDoItem(String mItemDescription){
        Log.d(TAG, TAG + ": Constructor starts");
        this.mItemDescription = mItemDescription;

        //by default item's date is set to be the EOD of ongoing day
        this.mItemDate = Utilities.getEndOfDay();
        //by default item is marked to be ONGOING
        this.mItemCategory = R.drawable.ic_ongoing;
        //by default item is given a random ID
        this.mItemID = UUID.randomUUID();
        Log.d(TAG, TAG + ": Constructor ends");
    }

    //overloaded constructor 1
    public ToDoItem(String mItemDescription, Date mItemDate){
        this(mItemDescription);
        this.mItemDate = mItemDate;

        //mark item category based on its date
        if(mItemDate.before(Utilities.getStartOfDay())){
            //mark item as overdue if its date is before start of the current day
            this.mItemCategory = R.drawable.ic_overdue;
        }
        else if(mItemDate.after(Utilities.getEndOfDay()) && !mItemDate.equals(Utilities.getEndOfDay())){
            //mark item as upcoming if its date is after end of the current day
            this.mItemCategory = R.drawable.ic_upcoming;
            Log.d(TAG, TAG + ": Parametes set by default Constructor modified");
        }
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
