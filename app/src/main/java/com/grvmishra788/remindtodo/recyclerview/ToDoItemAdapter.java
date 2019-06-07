package com.grvmishra788.remindtodo.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grvmishra788.remindtodo.R;
import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.basic.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ToDoItemViewHolder> {

    //constant Class TAG
    private static final String TAG = ToDoItemAdapter.class.getName();

    //Variable to store context from which Adapter has been called
    private Context mContext;

    //Variable for accessing ToDoItems List in  ToDoItemAdapter
    private ArrayList<ToDoItem> mToDoItems;

    //Variable for accessing SharedPreferences in ToDoItemAdapter
    private SharedPreferences mSharedPreferences;

    //Variables to store last deleted item incase of undo
    private ToDoItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public void markToDoCompleted(int position) {
        mToDoItems.get(position).setmItemCategory(R.drawable.ic_finished);
    }


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
    public ToDoItemAdapter(Context mContext, SharedPreferences mSharedPreferences, ArrayList<ToDoItem> mToDoItems){
        Log.d(TAG, TAG + ": Constructor starts");
        this.mContext = mContext;
        this.mSharedPreferences = mSharedPreferences;
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

    public Context getContext() {
        return this.mContext;
    }

    public void deleteItem(int position) {

        Log.d(TAG, "deleteItem() called for postition "+Integer.toString(position)+"-th item in ToDoItem List");
        mRecentlyDeletedItem = mToDoItems.get(position);
        mRecentlyDeletedItemPosition = position;
        mToDoItems.remove(position);
        Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        Log.d(TAG, "Deleted item "+Integer.toString(position)+"-th item in ToDoItem List+SharedPreferences");
        notifyItemRemoved(position);

        //once an item is deleted our undo Snackbar should appear
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Log.d(TAG, "Undo Snackbar Shown");
        View view = ((Activity)mContext).findViewById(R.id.main_relative_layout);
        Snackbar snackbar = Snackbar.make(view, "1 ToDo Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        Log.d(TAG, "Undo Action started for "+Integer.toString(mRecentlyDeletedItemPosition)+ "-th item in ToDoItem List.");
        mToDoItems.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        Log.d(TAG, "Undo Action completed for "+Integer.toString(mRecentlyDeletedItemPosition)+ "-th item in ToDoItem List.");
    }
}
