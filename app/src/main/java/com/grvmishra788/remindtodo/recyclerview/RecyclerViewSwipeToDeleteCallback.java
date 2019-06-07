package com.grvmishra788.remindtodo.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.grvmishra788.remindtodo.R;

public class RecyclerViewSwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    //constant Class TAG
    private static final String TAG = RecyclerViewSwipeToDeleteCallback.class.getName();

    //variable to hold the Recycler view adapter
    private ToDoItemAdapter mToDoItemAdapter;

    //adding the icon and background as member variables,
    //we will draw our icon and background in the correct position as our RecyclerView item is swiped across the screen.
    private Drawable icon;
    private final ColorDrawable background;

    public RecyclerViewSwipeToDeleteCallback(ToDoItemAdapter mToDoItemAdapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        Log.d(TAG, TAG + ": Constructor starts");
        this.mToDoItemAdapter = mToDoItemAdapter;
        icon = ContextCompat.getDrawable(mToDoItemAdapter.getContext(), R.drawable.ic_delete_todoitem);
        background = new ColorDrawable(Color.RED);
        Log.d(TAG, TAG + ": Constructor ends");
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();

        if(i==ItemTouchHelper.LEFT) {
            Log.d(TAG, "onSwiped() called at position: "+Integer.toString(position)+" in RecyclerView in LEFT dir");
            mToDoItemAdapter.deleteItem(position);
        }
        else{
            if(i==ItemTouchHelper.RIGHT){
                mToDoItemAdapter.markToDoCompleted(position);
            }
            Log.d(TAG, "onSwiped() called at position: "+Integer.toString(position)+" in RecyclerView in RIGHT/NO SWIPE dir");
            mToDoItemAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        Log.d(TAG, "onChildDraw() called!");
        //backgroundCornerOffset is used to push the background behind the edge of the parent view
        // so that it appears underneath the rounded corners
        int backgroundCornerOffset = 20;

        //if statement that covers the left, right, and no swipe cases.
        if (dX < 0) { // Swiping to the left

            //set the bounds for icon
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            //set the bounds for background
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
        else { // Swiping to the right or no swipe
            icon.setBounds(0,0,0, 0);
            background.setBounds(0, 0, 0, 0);
        }
        //draw background & icon onto the canvas.
        background.draw(c);
        icon.draw(c);
        Log.d(TAG, "onChildDraw() ended!");
    }
}
