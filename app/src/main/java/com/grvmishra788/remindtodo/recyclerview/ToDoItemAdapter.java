package com.grvmishra788.remindtodo.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grvmishra788.remindtodo.R;
import com.grvmishra788.remindtodo.basic.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ToDoItemViewHolder> {

    private static final String TAG = ToDoItemAdapter.class.getName();

    //Variable for ToDoItems List in  ToDoItemAdapter
    private ArrayList<ToDoItem> mToDoItems;

    //ToDoItemViewHolder nested class : holds RecyclerView elements defined in layout_todoitem.xml
    public static class ToDoItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1, mTextView2;

        public ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    //Constructor: binds ToDoItem object data to ToDoItemAdapter
    public ToDoItemAdapter(ArrayList<ToDoItem> mToDoItems){
        Log.d(TAG, TAG + ": Constructor starts");
        this.mToDoItems = mToDoItems;
        Log.d(TAG, TAG + ": Constructor ends");
    }

    @NonNull
    @Override
    public ToDoItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        Log.d(TAG, "Started creating View Holder for layout_todoitem.");
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_todoitem, viewGroup, false);
        ToDoItemViewHolder mToDoItemViewHolder = new ToDoItemViewHolder(mView);
        Log.d(TAG, "Completed creating View Holder for layout_todoitem.");
        return mToDoItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoItemViewHolder mToDoItemViewHolder, int index) {
        Log.d(TAG, "Started binding corresponding View Holder to "+Integer.toString(index)+"-th ToDoItem.");
        ToDoItem currentToDoItem = mToDoItems.get(index);
        mToDoItemViewHolder.mImageView.setImageResource(currentToDoItem.getmItemCategory());
        mToDoItemViewHolder.mTextView1.setText(currentToDoItem.getmItemDescription());
        mToDoItemViewHolder.mTextView2.setText(new SimpleDateFormat("MMM dd, YYYY - hh:mm:ss").format(currentToDoItem.getmItemDate()).toString().trim());
        Log.d(TAG, "Completed binding corresponding View Holder to "+Integer.toString(index)+"-th ToDoItem.");
    }

    @Override
    public int getItemCount() {
        return mToDoItems.size();
    }
}
