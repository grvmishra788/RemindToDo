package com.grvmishra788.remindtodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity;
import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.basic.Utilities;
import com.grvmishra788.remindtodo.recyclerview.OnToDoItemClickListener;
import com.grvmishra788.remindtodo.recyclerview.RecyclerViewSwipeToDeleteCallback;
import com.grvmishra788.remindtodo.recyclerview.ToDoItemAdapter;
import com.grvmishra788.remindtodo.reminder.ReminderAlertReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DATE;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DESCRIPTION;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_POSITION;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_REMINDER;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_TASK_FINISHED;

public class MainFragment extends Fragment implements SearchView.OnQueryTextListener {

    //contants
    private static final String TAG = MainFragment.class.getName();     //constant Class TAG
    public static final String FRAGMENT_CATEGORY = "com.grvmishra788.remindtodo.FRAGMENT_CATEGORY";
    public static final String COMPARATOR_TYPE = "com.grvmishra788.remindtodo.COMPARATOR_TYPE";
    public static final int ADD_TO_DO_ITEM = 1;
    public static final int EDIT_TO_DO_ITEM = 2;

    //recyclerView variables
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    //ToDoItems list
    private ArrayList<ToDoItem> mToDoItems;

    //SharedPreferences variable
    private SharedPreferences mSharedPreferences;

    //FloatingActionButton variable
    private FloatingActionButton mButton;

    //Fragment category
    private int mCategory;

    //Variable to store emptyView
    private TextView emptyView;

    //Variable to store Comparator object
    Comparator<ToDoItem> mToDoItemComparator;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get fragment category from bundle
        mCategory = getArguments().getInt(FRAGMENT_CATEGORY, -1);

        //get comparator type from bundle
        getComparator(getArguments().getInt(COMPARATOR_TYPE, 0));

        //inflate view
        View view = layoutInflater.inflate(R.layout.fragment_main, container, false);

        //init SharedPreferences variable
        mSharedPreferences = getContext().getSharedPreferences("ToDoList Shared Preferences", MODE_PRIVATE);

        //load mTodoItems From Shared Preferences
        mToDoItems = Utilities.loadToDoListFromSharedPreferences(mSharedPreferences);

        //init mToDoItems if there are no ToDos saved already
        if (mToDoItems == null) {
            mToDoItems = new ArrayList<>();
        } else {
            //else update mToDoItems
            for (ToDoItem mToDoITem : mToDoItems) {
                mToDoITem.updateToDoItemCategory();
            }
            Collections.sort(mToDoItems, mToDoItemComparator);
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        }

        //On Button click, save current ToDoItem if there is some text present in mEditText
        //else popup a toast to notify the user
        mButton = (FloatingActionButton) view.findViewById(R.id.addToDoITemBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mToDoItemIntent = new Intent(getContext(), AddOrEditToDoItemActivity.class);
                startActivityForResult(mToDoItemIntent, ADD_TO_DO_ITEM);
            }
        });

        //init EmptyView variable -> view to show if recyclerView is empty
        emptyView = (TextView) view.findViewById(R.id.emptyView);

        //init recyclerView variables
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);    //hasFixedSize=true increases app performance as Recyclerview is not going to change in size
        mRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewAdapter = new ToDoItemAdapter(getContext(), mSharedPreferences, mToDoItems, mCategory);
        mRecyclerViewAdapter.registerAdapterDataObserver(observer); //register data observer for recyclerView
        mRecyclerview.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerview.setAdapter(mRecyclerViewAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new RecyclerViewSwipeToDeleteCallback((ToDoItemAdapter) mRecyclerViewAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);

        ((ToDoItemAdapter) mRecyclerViewAdapter).setOnToDoItemClickListener(new OnToDoItemClickListener() {
            @Override
            public void onToDoItemClick(int position) {
                ToDoItem mToDoItem = mToDoItems.get(position);
                Intent mEditToDoItemIntent = new Intent(getContext(), AddOrEditToDoItemActivity.class);
                mEditToDoItemIntent.putExtra(EXTRA_DESCRIPTION, mToDoItem.getmItemDescription());
                mEditToDoItemIntent.putExtra(EXTRA_DATE, mToDoItem.getmItemDate().getTime());
                mEditToDoItemIntent.putExtra(EXTRA_REMINDER, mToDoItem.getmItemSetReminder());
                mEditToDoItemIntent.putExtra(EXTRA_POSITION, position);
                mEditToDoItemIntent.putExtra(EXTRA_TASK_FINISHED, (mToDoItem.getmItemCategory()==R.drawable.ic_finished)?true:false);
                startActivityForResult(mEditToDoItemIntent, EDIT_TO_DO_ITEM);
            }
        });
        checkIfEmpty();
        return view;
    }

    private void getComparator(int comparatorType) {
        // 0 -> Alphabetical, 1-> Due Date
        switch (comparatorType) {
            case 0:
                mToDoItemComparator = new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem t1, ToDoItem t2) {
                        return t1.getmItemDescription().toLowerCase().compareTo(t2.getmItemDescription().toLowerCase());
                    }
                };
                break;
            case 1:
                mToDoItemComparator = new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem t1, ToDoItem t2) {
                        int ans = 0;
                        if (t1.getmItemDate().before(t2.getmItemDate())) {
                            ans = -1;
                        } else if (t1.getmItemDate().after(t2.getmItemDate())) {
                            ans = 1;
                        }
                        return ans;
                    }
                };
                break;
            default:
                //by default choose alphabetical sorting
                mToDoItemComparator = new Comparator<ToDoItem>() {
                    @Override
                    public int compare(ToDoItem t1, ToDoItem t2) {
                        return t1.getmItemDescription().toLowerCase().compareTo(t2.getmItemDescription().toLowerCase());
                    }
                };
                break;
        }
    }

    //init data observer for recyclerView
    private final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            Log.d(TAG, "onChanged() called for recyclerView observer!");
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            Log.d(TAG, "onItemRangeInserted() called for recyclerView observer!");
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            Log.d(TAG, "onItemRangeRemoved() called for recyclerView observer!");
            checkIfEmpty();
        }
    };

    //method to take necessary action based on whether recyclerView is empty or not
    void checkIfEmpty() {
        Log.d(TAG, "checkIfEmpty() called!");
        String mFragmentType = getFragmentType();
        final boolean emptyViewVisible = ((ToDoItemAdapter) mRecyclerViewAdapter).hasItemForCurrentCategory();
        emptyView.setText("You dont have any" + mFragmentType + " ToDos!");
        emptyView.setVisibility(!emptyViewVisible ? VISIBLE : GONE);
        mRecyclerview.setVisibility(!emptyViewVisible ? GONE : VISIBLE);
        Log.d(TAG, "checkIfEmpty() completed!");
    }

    //function to get string representing the fragment category
    private String getFragmentType() {
        String res = "";
        switch (mCategory) {
            case R.drawable.ic_ongoing:
                res = " Ongoing";
                break;
            case R.drawable.ic_overdue:
                res = " Overdue";
                break;
            case R.drawable.ic_finished:
                res = " Finished";
                break;
            case R.drawable.ic_upcoming:
                res = " Upcoming";
                break;
        }
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult called for requestCode = " + Integer.toString(requestCode));
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TO_DO_ITEM && resultCode == RESULT_OK) {

            //obtain mToDoItemDescription & mToDoItemDate
            String mToDoItemDescription = data.getStringExtra(EXTRA_DESCRIPTION);
            Date mDate = new Date(data.getExtras().getLong(EXTRA_DATE));
            Boolean mItemSetReminder = data.getExtras().getBoolean(EXTRA_REMINDER);

            //add new ToDoItem to list & shared preferences
            ToDoItem mToDoItem = new ToDoItem(mToDoItemDescription, mDate, mItemSetReminder);
            mToDoItems.add(mToDoItem);
            Collections.sort(mToDoItems, mToDoItemComparator);
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
            Log.d(TAG, "onActivityResult completed for requestCode = " + Integer.toString(requestCode));

            //set Alarm for this new ToDoItem if required
            if (mItemSetReminder == true) {
                setAlarm(mToDoItem);
            }

            //update UI to show changes
            mRecyclerViewAdapter.notifyDataSetChanged();
        } else if (requestCode == EDIT_TO_DO_ITEM && resultCode == RESULT_OK) {

            int position = data.getIntExtra(EXTRA_POSITION, -1);
            if (position != -1) {

                String mToDoItemDescription = data.getStringExtra(EXTRA_DESCRIPTION);
                Date mDate = new Date(data.getExtras().getLong(EXTRA_DATE));
                Boolean mItemSetReminder = data.getExtras().getBoolean(EXTRA_REMINDER);
                Boolean mTaskFinished = data.getExtras().getBoolean(EXTRA_TASK_FINISHED);

                //edit ToDoItem in list & save changes to shared preferences
                ToDoItem mItemToChange = mToDoItems.get(position);

                //if already an alarm was there, cancel last alarm so that modifications can be handled
                if (mItemToChange.getmItemSetReminder() == true) {
                    cancelAlarm(mItemToChange);
                }

                //update description and date
                mItemToChange.setmItemDescription(mToDoItemDescription);
                mItemToChange.setmItemDate(mDate);
                if(!mTaskFinished){ //if Task Finished switch is turned OFF
                    //hack to deal with updating finished TODOs
                    mItemToChange.setmItemCategory(R.drawable.ic_overdue);
                    //update category
                    mItemToChange.updateToDoItemCategory();
                    //set reminder
                    mItemToChange.setmItemSetReminder(mItemSetReminder);
                } else {
                    //set category to finished
                    mItemToChange.setmItemCategory(R.drawable.ic_finished);
                }
                mToDoItems.set(position, mItemToChange);
                Collections.sort(mToDoItems, mToDoItemComparator);
                Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);

                //set Alarm for this updated ToDoItem if required
                if (mItemSetReminder == true) {
                    setAlarm(mItemToChange);
                }

                //update UI to show changes
                mRecyclerViewAdapter.notifyDataSetChanged();

                Log.d(TAG, "onActivityResult completed for requestCode = " + Integer.toString(requestCode));
            } else {
                Toast.makeText(getContext(), "ToDo Item can't be updated", Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(getContext(), "ToDo Item not saved", Toast.LENGTH_LONG);
        }
    }

    private void setAlarm(ToDoItem mToDoItem) {

        Log.d(TAG, "setAlarm() called for  - " + mToDoItem.getmItemDescription());

        //init AlarmManager
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        //package intent
        Intent intent = new Intent(getContext(), ReminderAlertReceiver.class);
        intent.putExtra(EXTRA_DESCRIPTION, mToDoItem.getmItemDescription());

        //get unique alarmID from ToDoItem UUID
        int alarmID = mToDoItem.getmItemID().hashCode();

        //turn alarm ON
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmID, intent, 0);
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mToDoItem.getmItemDate());
        if (pendingIntent != null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);

        Log.d(TAG, "setAlarm() completed for  - " + mToDoItem.getmItemDescription());
    }

    private void cancelAlarm(ToDoItem mToDoItem) {

        Log.d(TAG, "cancelAlarm() Called for  - " + mToDoItem.getmItemDescription());

        //init AlarmManager
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        //package intent
        Intent intent = new Intent(getContext(), ReminderAlertReceiver.class);
        intent.putExtra(EXTRA_DESCRIPTION, mToDoItem.getmItemDescription());

        //get unique alarmID from ToDoItem UUID
        int alarmID = mToDoItem.getmItemID().hashCode();

        //turn alarm OFF
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmID, intent, 0);
        if (pendingIntent != null)
            alarmManager.cancel(pendingIntent);

        Log.d(TAG, "cancelAlarm() completed for  - " + mToDoItem.getmItemDescription());
    }


    //function-1 to implement SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    //function-2 to implement SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextChange(String s) {
        //get user input in lowercase
        String userInput = s.toLowerCase();
        //create a new ToDoItem list
        List<ToDoItem> matchedList = new ArrayList<>();
        //add matching items to this new list
        for(ToDoItem mToDoItem: mToDoItems){
            if(mToDoItem.getmItemDescription().toLowerCase().contains(userInput)){
                matchedList.add(mToDoItem);
            }
        }
        //update list in recyclerView adapter
        ((ToDoItemAdapter)mRecyclerViewAdapter).updateList(matchedList);
        return true;
    }
}
