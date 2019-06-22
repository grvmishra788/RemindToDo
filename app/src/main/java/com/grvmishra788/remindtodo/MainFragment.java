package com.grvmishra788.remindtodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DATE;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DESCRIPTION;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_POSITION;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_REMINDER;

public class MainFragment extends Fragment {

    //contants
    private static final String TAG = MainFragment.class.getName();     //constant Class TAG
    public static final String FRAGMENT_CATEGORY = "com.grvmishra788.remindtodo.FRAGMENT_CATEGORY";
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

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getInt(FRAGMENT_CATEGORY, -1);
        View view = layoutInflater.inflate(R.layout.fragment_main, container, false);

        //init SharedPreferences variable
        mSharedPreferences = getContext().getSharedPreferences("ToDoList Shared Preferences", MODE_PRIVATE);

        //load mTodoItems From Shared Preferences
        mToDoItems = Utilities.loadToDoListFromSharedPreferences(mSharedPreferences);

        //init mToDoItems if there are no ToDos saved already
        if (mToDoItems == null) {
            mToDoItems = new ArrayList<>();
        }
        else{
            //else update mToDoItems
            for(ToDoItem mToDoITem: mToDoItems){
                mToDoITem.updateToDoItemCategory();
            }
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        }

        //On Button click, save current ToDoItem if there is some text present in mEditText
        //else popup a toast to notify the user
        mButton = (FloatingActionButton) view.findViewById(R. id.addToDoITemBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mToDoItemIntent = new Intent(getContext(), AddOrEditToDoItemActivity.class);
                startActivityForResult(mToDoItemIntent, ADD_TO_DO_ITEM);
            }
        });

        //init recyclerView variables
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);    //hasFixedSize=true increases app performance as Recyclerview is not going to change in size
        mRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewAdapter = new ToDoItemAdapter(getContext(), mSharedPreferences, mToDoItems, mCategory);
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
                startActivityForResult(mEditToDoItemIntent, EDIT_TO_DO_ITEM);
            }
        });

        return view;
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
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
            Log.d(TAG, "onActivityResult completed for requestCode = " + Integer.toString(requestCode));

            //set Alarm for this new ToDoItem if required
            if(mItemSetReminder==true){
                setAlarm(mToDoItem);
            }

            //update UI to show changes
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
        else if (requestCode == EDIT_TO_DO_ITEM && resultCode == RESULT_OK) {

            int position = data.getIntExtra(EXTRA_POSITION, -1);
            if (position!=-1) {

                String mToDoItemDescription = data.getStringExtra(EXTRA_DESCRIPTION);
                Date mDate = new Date(data.getExtras().getLong(EXTRA_DATE));
                Boolean mItemSetReminder = data.getExtras().getBoolean(EXTRA_REMINDER);

                //edit ToDoItem in list & save changes to shared preferences
                ToDoItem mItemToChange = mToDoItems.get(position);

                //if already an alarm was there, cancel last alarm so that modifications can be handled
                if(mItemToChange.getmItemSetReminder()==true){
                    cancelAlarm(mItemToChange);
                }

                //update description and date
                mItemToChange.setmItemDescription(mToDoItemDescription);
                mItemToChange.setmItemDate(mDate);
                mItemToChange.setmItemSetReminder(mItemSetReminder);
                mItemToChange.updateToDoItemCategory();
                mToDoItems.set(position, mItemToChange);
                Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);

                //set Alarm for this updated ToDoItem if required
                if(mItemSetReminder==true){
                    setAlarm(mItemToChange);
                }

                //update UI to show changes
                mRecyclerViewAdapter.notifyItemChanged(position);

                Log.d(TAG, "onActivityResult completed for requestCode = " + Integer.toString(requestCode));
            }
            else {
                Toast.makeText(getContext(), "ToDo Item can't be updated", Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(getContext(), "ToDo Item not saved", Toast.LENGTH_LONG);
        }
    }

    private void setAlarm(ToDoItem mToDoItem) {

        Log.d(TAG, "setAlarm() called for  - "+mToDoItem.getmItemDescription());

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
        if(pendingIntent!=null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);

        Log.d(TAG, "setAlarm() completed for  - "+mToDoItem.getmItemDescription());
    }

    private void cancelAlarm(ToDoItem mToDoItem) {

        Log.d(TAG, "cancelAlarm() Called for  - "+mToDoItem.getmItemDescription());

        //init AlarmManager
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        //package intent
        Intent intent = new Intent(getContext(), ReminderAlertReceiver.class);
        intent.putExtra(EXTRA_DESCRIPTION, mToDoItem.getmItemDescription());

        //get unique alarmID from ToDoItem UUID
        int alarmID = mToDoItem.getmItemID().hashCode();

        //turn alarm OFF
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmID, intent, 0);
        if(pendingIntent!=null)
            alarmManager.cancel(pendingIntent);

        Log.d(TAG, "cancelAlarm() completed for  - "+mToDoItem.getmItemDescription());
    }
}
