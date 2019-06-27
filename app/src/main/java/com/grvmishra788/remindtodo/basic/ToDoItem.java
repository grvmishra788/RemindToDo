package com.grvmishra788.remindtodo.basic;

import android.util.Log;

import com.grvmishra788.remindtodo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ToDoItem {

    //constant Class TAG
    private static final String TAG = ToDoItem.class.getName();

    //static Calendar instance to get current time
    private static Calendar mCalendar;

    //ToDoItem variables
    private String mItemDescription;
    private Date mItemDate;
    private int mItemCategory;
    private UUID mItemID;
    private boolean mItemSetReminder;

    //constructor
    public ToDoItem(String mItemDescription){
        Log.d(TAG, TAG + ": Constructor starts");
        this.mItemDescription = mItemDescription;

        //by default item's date is set to be the SOND of ongoing day
        this.mItemDate = Utilities.getStartOfNextDay();
        //by default item is marked to be ONGOING
        this.mItemCategory = R.drawable.ic_ongoing;
        //by default item is given a random ID
        this.mItemID = UUID.randomUUID();
        //by default no reminder added to ToDoItem
        this.mItemSetReminder = false;
        Log.d(TAG, TAG + ": Constructor ends");
    }

    //overloaded constructor 1
    public ToDoItem(String mItemDescription, Date mItemDate, Boolean mItemSetReminder){
        this(mItemDescription);
        this.mItemDate = mItemDate;
        this.mItemSetReminder = mItemSetReminder;

        //mark item category based on its date
        if(mItemDate.before(Utilities.getStartOfDay())){
            //mark item as overdue if its date is before start of the current day
            this.mItemCategory = R.drawable.ic_overdue;
        }
        else if(mItemDate.after(Utilities.getStartOfNextDay())){
            //mark item as upcoming if its date is after end of the current day
            this.mItemCategory = R.drawable.ic_upcoming;
            Log.d(TAG, TAG + ": Parametes set by default Constructor modified");
        }
        removeReminderIfRequired();
    }

    //remove ToDoItem reminder if it has been completed OR it has already gone off i.e. its overdue
    public void removeReminderIfRequired(){
        if(mItemCategory==R.drawable.ic_overdue || mItemCategory==R.drawable.ic_finished)
            mItemSetReminder = false;
    }

    //update ToDoItem category based on current date
    public void updateToDoItemCategory(){
        mCalendar = Calendar.getInstance();
        switch (mItemCategory){
            case R.drawable.ic_ongoing:
                if(mCalendar.getTime().after(mItemDate)) {
                    //ongoing + date_passed = overdue
                    mItemCategory = R.drawable.ic_overdue;
                }
                else if(mItemDate.after(Utilities.getStartOfNextDay())){
                    //ongoing + new date after SOND
                    mItemCategory = R.drawable.ic_upcoming;
                }
                break;
            case R.drawable.ic_upcoming:
                if(mCalendar.getTime().after(mItemDate)) {
                    //upcoming + date_passed = overdue
                    mItemCategory = R.drawable.ic_overdue;
                }
                else if(mItemDate.after(Utilities.getStartOfDay()) && mItemDate.before(Utilities.getStartOfNextDay())) {
                    //upcoming + date after SOD but before SOND  = ongoing
                    mItemCategory = R.drawable.ic_ongoing;
                }
                break;
            case R.drawable.ic_overdue:
                if(mItemDate.after(Utilities.getStartOfNextDay())) {
                    //overdue + new date after SOND
                    mItemCategory = R.drawable.ic_upcoming;
                }
                else if(mItemDate.after(Utilities.getStartOfDay()) && !mCalendar.getTime().after(mItemDate)){
                    //overdue + new date after SOND but before SOD + date not passed
                    mItemCategory = R.drawable.ic_ongoing;
                }
                break;
            default:
                break;
        }
        removeReminderIfRequired();
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

    public boolean getmItemSetReminder(){
        return mItemSetReminder;
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

    public void setmItemSetReminder(boolean mItemSetReminder){
        this.mItemSetReminder = mItemSetReminder;
        removeReminderIfRequired();
    }
}
