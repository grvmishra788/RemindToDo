package com.grvmishra788.remindtodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.recyclerview.ToDoItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<ToDoItem> mToDoItems = new ArrayList<>();
        mToDoItems.add(new ToDoItem("Shop groceries", new Date(Calendar.getInstance().YEAR, Calendar.getInstance().MONTH, Calendar.getInstance().DATE-1, 23, 59,59), R.drawable.ic_overdue));
        mToDoItems.add(new ToDoItem("Clean Clothes"));
        mToDoItems.add(new ToDoItem("Practice guitar"));

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);    //hasFixedSize=true increases app performance as Recyclerview is not going to change in size
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewAdapter = new ToDoItemAdapter(mToDoItems);

        mRecyclerview.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerview.setAdapter(mRecyclerViewAdapter);

    }
}
