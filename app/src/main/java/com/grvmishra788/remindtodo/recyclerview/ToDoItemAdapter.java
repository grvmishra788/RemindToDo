package com.grvmishra788.remindtodo.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grvmishra788.remindtodo.MainActivity;
import com.grvmishra788.remindtodo.R;
import com.grvmishra788.remindtodo.basic.ToDoItem;
import com.grvmishra788.remindtodo.basic.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.DATE_FORMAT_DAY_AND_DATE;

public class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemAdapter.ToDoItemViewHolder> {

    //constants
    private static final String TAG = ToDoItemAdapter.class.getName(); //constant Class TAG
    private static final int UNDO_TODO_COMPLETED = 0;
    private static final int UNDO_TODO_DELETED = 1;
    private static final int UNDO_TODO_ALREADY_COMPLETED = 2;

    //Variable to store context from which Adapter has been called
    private Context mContext;

    //Variable for accessing ToDoItems List in  ToDoItemAdapter
    private ArrayList<ToDoItem> mToDoItems;

    //Variable for accessing SharedPreferences in ToDoItemAdapter
    private SharedPreferences mSharedPreferences;

    //Variables to store last deleted item details incase of undo
    private ToDoItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    //Variables to store last completed item details incase of undo
    private int mRecentlyCompletedItemPosition, mRecentlyCompletedItemCategory;
    private Boolean mRecentlyCompletedItemReminder;

    //Variable to store OnToDoItemClickListener
    private OnToDoItemClickListener mOnToDoItemClickListener;

    //Variable to store type of fragment launching the adapter
    private int mCategory;

    //Variable to store selectedItem positions when launching Contextual action mode
    private TreeSet<Integer> selectedItems = new TreeSet<>();

    //ToDoItemViewHolder nested class : holds RecyclerView elements defined in layout_todoitem.xml
    public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        public CardView mRootView;
        public ImageView mImageView, mReminderActiveView;
        public TextView mTextView1, mTextView2;

        public ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.basicLayout);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mReminderActiveView = itemView.findViewById(R.id.reminderActiveView);
            //perform necessary ops if current item is clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (mOnToDoItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        mOnToDoItemClickListener.onToDoItemClick(position);
                    }
                }
            });

            //perform necessary ops if current item is long clicked
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (mOnToDoItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        mOnToDoItemClickListener.onToDoItemLongClick(position);
                    }
                    return true;
                }
            });
        }
    }

    //Constructor: binds ToDoItem object data to ToDoItemAdapter
    public ToDoItemAdapter(Context mContext, SharedPreferences mSharedPreferences, ArrayList<ToDoItem> mToDoItems, int mCategory) {
        Log.d(TAG, TAG + ": Constructor starts");
        this.mContext = mContext;
        this.mSharedPreferences = mSharedPreferences;
        this.mToDoItems = mToDoItems;
        this.mCategory = mCategory;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ToDoItemViewHolder mToDoItemViewHolder, int index) {
        Log.d(TAG, "Started binding corresponding View Holder to " + Integer.toString(index) + "-th ToDoItem.");
        ToDoItem currentToDoItem = mToDoItems.get(index);
        if(mCategory==-1 || mCategory == currentToDoItem.getmItemCategory()){
            //show itemView if ToDoItem belongs to current Fragment category
            //make sure itemView has correct params
            mToDoItemViewHolder.itemView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            mToDoItemViewHolder.itemView.setLayoutParams(params);

            //bind resources to view as per ToDoItem
            mToDoItemViewHolder.mImageView.setImageResource(currentToDoItem.getmItemCategory());
            mToDoItemViewHolder.mTextView1.setText(currentToDoItem.getmItemDescription());
            mToDoItemViewHolder.mTextView2.setText(new SimpleDateFormat(DATE_FORMAT_DAY_AND_DATE+", "+ MainActivity.getDefaultDateDisplayFormat()).format(currentToDoItem.getmItemDate()).toString().trim());
            if(currentToDoItem.getmItemSetReminder()==true){
                mToDoItemViewHolder.mReminderActiveView.setVisibility(View.VISIBLE);
                mToDoItemViewHolder.mReminderActiveView.setImageResource(R.drawable.ic_notifications_active);
            }
            else {
                mToDoItemViewHolder.mReminderActiveView.setVisibility(View.INVISIBLE);
            }
        }else {
            //hide itemView if ToDoItem doesnt belong to current Fragment category
            mToDoItemViewHolder.itemView.setVisibility(View.GONE);
            mToDoItemViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

        if (selectedItems.contains(index)){
            //if item is selected then,set foreground color of FrameLayout.
            mToDoItemViewHolder.mRootView.setForeground(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.colorControlActivated)));
        }
        else {
            //else remove selected item color.
            mToDoItemViewHolder.mRootView.setForeground(new ColorDrawable(ContextCompat.getColor(getContext(),android.R.color.transparent)));
        }

        Log.d(TAG, "Completed binding corresponding View Holder to " + Integer.toString(index) + "-th ToDoItem.");
    }

    @Override
    public int getItemCount() {
        return mToDoItems.size();
    }

    public boolean hasItemForCurrentCategory(){
        //Variable to store count of items in current category fragment
        Log.d(TAG, "hasItemForCurrentCategory() called!");
        boolean hasItem=false;
        for(int i=0;i<mToDoItems.size();i++){
            if(mCategory==-1 || mCategory == mToDoItems.get(i).getmItemCategory()){
                hasItem = true;
                break;
            }
        }
        Log.d(TAG, "hasItemForCurrentCategory() completed!");
        return hasItem;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void markToDoCompleted(int position) {
        Log.d(TAG, "markToDoCompleted() called for " + Integer.toString(position) + "-th item in ToDoItem List");
        if (mToDoItems.get(position).getmItemCategory() == R.drawable.ic_finished) {
            //once an item is completed our undo Snackbar should appear
            showUndoSnackbar(UNDO_TODO_ALREADY_COMPLETED);
        } else {
            mRecentlyCompletedItemCategory = mToDoItems.get(position).getmItemCategory();
            mRecentlyCompletedItemPosition = position;
            mRecentlyCompletedItemReminder = mToDoItems.get(position).getmItemSetReminder();
            mToDoItems.get(position).setmItemCategory(R.drawable.ic_finished);
            mToDoItems.get(position).removeReminderIfRequired();
            Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);

            //once an item is completed our undo Snackbar should appear
            showUndoSnackbar(UNDO_TODO_COMPLETED);
            Log.d(TAG, "markToDoCompleted() complted for " + Integer.toString(position) + "-th item in ToDoItem List");
        }
    }

    public void deleteItem(int position) {
        Log.d(TAG, "deleteItem() called for " + Integer.toString(position) + "-th item in ToDoItem List");
        mRecentlyDeletedItem = mToDoItems.get(position);
        mRecentlyDeletedItemPosition = position;
        mToDoItems.remove(position);
        Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        Log.d(TAG, "Deleted item " + Integer.toString(position) + "-th item in ToDoItem List+SharedPreferences");
        notifyItemRemoved(position);

        //once an item is deleted our undo Snackbar should appear
        showUndoSnackbar(UNDO_TODO_DELETED);
    }

    private void showUndoSnackbar(int type) {
        Log.d(TAG, "Undo Snackbar Shown");
        View view = ((Activity) mContext).findViewById(R.id.main_relative_layout);
        Snackbar snackbar;
        if (type == UNDO_TODO_COMPLETED) {//If ToDoITem Completed
            snackbar = Snackbar.make(view, "1 ToDo Completed", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    undoComplete();
                }
            });
        } else if (type == UNDO_TODO_DELETED) { //if ToDoItem Deleted
            snackbar = Snackbar.make(view, "1 ToDo Deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    undoDelete();
                }
            });
        } else { //if ToDoItem Already Completed
            snackbar = Snackbar.make(view, "ToDo already completed", Snackbar.LENGTH_LONG);
        }
        snackbar.setActionTextColor(Color.parseColor("#D81B60"));
        snackbar.show();
    }

    //function to undo most recent ToDoItem completion
    private void undoComplete() {
        Log.d(TAG, "Undo Complete Action started for " + Integer.toString(mRecentlyDeletedItemPosition) + "-th item in ToDoItem List.");
        mToDoItems.get(mRecentlyCompletedItemPosition).setmItemCategory(mRecentlyCompletedItemCategory);
        mToDoItems.get(mRecentlyCompletedItemPosition).setmItemSetReminder(mRecentlyCompletedItemReminder);
        Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        notifyItemChanged(mRecentlyCompletedItemPosition);
        Log.d(TAG, "Undo Complete Action completed for " + Integer.toString(mRecentlyDeletedItemPosition) + "-th item in ToDoItem List.");

    }

    //function to undo most recent ToDoItem deletion
    private void undoDelete() {
        Log.d(TAG, "Undo Delete Action started for " + Integer.toString(mRecentlyDeletedItemPosition) + "-th item in ToDoItem List.");
        mToDoItems.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        Utilities.saveToDoListToSharedPreferences(mSharedPreferences, mToDoItems);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        Log.d(TAG, "Undo Delete Action completed for " + Integer.toString(mRecentlyDeletedItemPosition) + "-th item in ToDoItem List.");
    }

    //mutator method to set mOnToDoItemClickListener
    public void setOnToDoItemClickListener(OnToDoItemClickListener mOnToDoItemClickListener) {
        this.mOnToDoItemClickListener = mOnToDoItemClickListener;
    }

    //method to update mToDoItems List while toDoItem search is going on
    public void updateList(List<ToDoItem> matchingList){
        Log.d(TAG, "updateList() called");
        mToDoItems = new ArrayList<>();
        mToDoItems.addAll(matchingList);
        notifyDataSetChanged();
        Log.d(TAG, "updateList() completed");
    }

    //method to update selected items
    public void setSelectedItems(TreeSet<Integer> selectedItems) {
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

}