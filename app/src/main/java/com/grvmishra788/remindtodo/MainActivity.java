package com.grvmishra788.remindtodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.basic.Utilities;
import com.grvmishra788.remindtodo.recyclerview.ToDoItemAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    //recyclerView variables
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    //EditText variable
    private EditText mEditText;

    //Button variable
    private Button mButton;

    //ToDoItems list
    ArrayList<ToDoItem> mToDoItems;

    //SharedPreferences variable
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int SharedPreferences variable
        mSharedPreferences = getSharedPreferences("ToDoList Shared Preferences", MODE_PRIVATE);

        //load mTodoItems From Shared Preferences
        mToDoItems = Utilities.loadToDoListFromSharedPreferences(mSharedPreferences);

        //init mToDoItems if there are no ToDos saved already
        if(mToDoItems==null){
            mToDoItems = new ArrayList<>();
        }

        //init EditText variable
        mEditText = (EditText) findViewById(R.id.editText);

        //init Button variable
        mButton = (Button) findViewById(R.id.saveBtn);

        //On Button click, save current ToDoItem if there is some text present in mEditText
        //else popup a toast to notify the user
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obtain ToDoItemDescription from mEditTExt
                String mToDoItemDescription = mEditText.getText().toString();

                if(mToDoItemDescription.equals("")){
                    //if this description is null, popup a toast to notify the user
                    Toast.makeText(getApplicationContext(), "No ToDo to save", LENGTH_SHORT).show();
                }
                else{
                    //else save current ToDoItem & clear mEditText
                    mToDoItems.add(new ToDoItem(mToDoItemDescription));
                    Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
                    mEditText.getText().clear();
                }

                //Hide keyboard once button has done its job
                InputMethodManager mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        //init recyclerView variables
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);    //hasFixedSize=true increases app performance as Recyclerview is not going to change in size
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewAdapter = new ToDoItemAdapter(mToDoItems);
        mRecyclerview.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerview.setAdapter(mRecyclerViewAdapter);

    }
}
