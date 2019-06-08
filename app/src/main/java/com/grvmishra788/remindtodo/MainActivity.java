package com.grvmishra788.remindtodo;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grvmishra788.remindtodo.add_todo.AddToDoItem;
import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.basic.Utilities;
import com.grvmishra788.remindtodo.recyclerview.RecyclerViewSwipeToDeleteCallback;
import com.grvmishra788.remindtodo.recyclerview.ToDoItemAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    //contants
    private static final String TAG = MainActivity.class.getName();     //constant Class TAG
    public static final int ADD_TO_DO_ITEM = 1;

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
        if(mToDoItems==null){
            mToDoItems = new ArrayList<>();
        }

        //On Button click, save current ToDoItem if there is some text present in mEditText
        //else popup a toast to notify the user
        mButton = (FloatingActionButton) findViewById(R.id.addToDoITemBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mToDoItemIntent = new Intent(MainActivity.this, AddToDoItem.class);
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult called for requestCode = "+Integer.toString(requestCode));
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TO_DO_ITEM && resultCode == RESULT_OK){
            //obtain ToDoItemDescription from mEditTExt
            String mToDoItemDescription = data.getStringExtra(AddToDoItem.EXTRA_DESCRIPTION);
            Date mDate = new Date(data.getExtras().getLong(AddToDoItem.EXTRA_DATE));
            if(mDate!=null){
                mToDoItems.add(new ToDoItem(mToDoItemDescription, mDate));
            }
            else{
                mToDoItems.add(new ToDoItem(mToDoItemDescription));
            }
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
            Log.d(TAG, "onActivityResult completed for requestCode = "+Integer.toString(requestCode));
        }
    }
}
