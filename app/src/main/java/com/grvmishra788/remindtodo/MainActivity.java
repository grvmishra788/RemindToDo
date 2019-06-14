package com.grvmishra788.remindtodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity;
import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.basic.Utilities;
import com.grvmishra788.remindtodo.recyclerview.OnToDoItemClickListener;
import com.grvmishra788.remindtodo.recyclerview.RecyclerViewSwipeToDeleteCallback;
import com.grvmishra788.remindtodo.recyclerview.ToDoItemAdapter;

import java.util.ArrayList;
import java.util.Date;

import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DATE;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DESCRIPTION;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_POSITION;
import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_REMINDER;

public class MainActivity extends AppCompatActivity {

    //contants
    private static final String TAG = MainActivity.class.getName();     //constant Class TAG
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init SharedPreferences variable
        mSharedPreferences = getSharedPreferences("ToDoList Shared Preferences", MODE_PRIVATE);

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
        mButton = (FloatingActionButton) findViewById(R.id.addToDoITemBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mToDoItemIntent = new Intent(MainActivity.this, AddOrEditToDoItemActivity.class);
                startActivityForResult(mToDoItemIntent, ADD_TO_DO_ITEM);
            }
        });

        //init recyclerView variables
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);    //hasFixedSize=true increases app performance as Recyclerview is not going to change in size
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewAdapter = new ToDoItemAdapter(this, mSharedPreferences, mToDoItems);
        mRecyclerview.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerview.setAdapter(mRecyclerViewAdapter);

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new RecyclerViewSwipeToDeleteCallback((ToDoItemAdapter) mRecyclerViewAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);

        ((ToDoItemAdapter) mRecyclerViewAdapter).setOnToDoItemClickListener(new OnToDoItemClickListener() {
            @Override
            public void onToDoItemClick(int position) {
                ToDoItem mToDoItem = mToDoItems.get(position);
                Intent mEditToDoItemIntent = new Intent(MainActivity.this, AddOrEditToDoItemActivity.class);
                mEditToDoItemIntent.putExtra(EXTRA_DESCRIPTION, mToDoItem.getmItemDescription());
                mEditToDoItemIntent.putExtra(EXTRA_DATE, mToDoItem.getmItemDate().getTime());
                mEditToDoItemIntent.putExtra(EXTRA_REMINDER, mToDoItem.getmItemSetReminder());
                mEditToDoItemIntent.putExtra(EXTRA_POSITION, position);
                startActivityForResult(mEditToDoItemIntent, EDIT_TO_DO_ITEM);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult called for requestCode = " + Integer.toString(requestCode));
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TO_DO_ITEM && resultCode == RESULT_OK) {

            //obtain mToDoItemDescription & mToDoItemDate
            String mToDoItemDescription = data.getStringExtra(EXTRA_DESCRIPTION);
            Date mDate = new Date(data.getExtras().getLong(EXTRA_DATE));
            Boolean mItemSetReminder = data.getExtras().getBoolean(EXTRA_REMINDER);

            //add new ToDoItem to list & shared preferences
            mToDoItems.add(new ToDoItem(mToDoItemDescription, mDate, mItemSetReminder));
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
            Log.d(TAG, "onActivityResult completed for requestCode = " + Integer.toString(requestCode));

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
                mItemToChange.setmItemDescription(mToDoItemDescription);
                mItemToChange.setmItemDate(mDate);
                mItemToChange.setmItemSetReminder(mItemSetReminder);
                mItemToChange.updateToDoItemCategory();
                mToDoItems.set(position, mItemToChange);
                Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);

                //update UI to show changes
                mRecyclerViewAdapter.notifyItemChanged(position);

                Log.d(TAG, "onActivityResult completed for requestCode = " + Integer.toString(requestCode));
            }
            else {
                Toast.makeText(this, "ToDo Item can't be updated", Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(this, "ToDo Item not saved", Toast.LENGTH_LONG);
        }
    }
}
