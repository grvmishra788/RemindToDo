package com.grvmishra788.remindtodo.add_todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.grvmishra788.remindtodo.MainActivity;
import com.grvmishra788.remindtodo.R;
import com.grvmishra788.remindtodo.basic.ToDoItem;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class AddToDoItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //constant strings
    private static final String TAG = AddToDoItem.class.getName();  //constant Class TAG
    public static final String EXTRA_DESCRIPTION = "com.grvmishra788.remindtodo.add_todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.grvmishra788.remindtodo.add_todo.EXTRA_DATE";

    //EditText variables
    private EditText mEditText;
    private TextView mEditDate;

    //Button variables
    private ImageButton mDateButton;

    //DAte variables
    Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called for "+TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);

        //init EditText variables
        mEditText = (EditText) findViewById(R.id.editText);
        mEditDate = (TextView) findViewById(R.id.editDate);

        //init Date Button variable & set its onClick Listener
        mDateButton = (ImageButton) findViewById(R.id.saveDate);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickListener called for mDateButton");
                DialogFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getSupportFragmentManager(), "Date Picker Dialog");
                Log.d(TAG, "onClickListener finished for mDateButton");
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_activity);
        setTitle("Add ToDo Item");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        Log.d(TAG, "onCreateOptionsMenu() called for AddToDoItem menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_todoitem_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called for AddToDoItem menu");
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.saveToDoItem:
                Log.d(TAG, "saveToDoItem selected for AddToDoItem menu");
                saveToDoItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "OnDateSetListener() called");

        //get year, month and day from calendar instance
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);

        //create date object
        mDate = mCalendar.getTime();

        //convert date to string & display in text view
        String mDateString = DateFormat.getDateInstance(DateFormat.FULL).format(mDate);
        mEditDate.setText(mDateString);

        Log.d(TAG, "OnDateSetListener() call completed");
    }

    private void saveToDoItem(){
        Log.d(TAG, "saveToDoItem() called.");

        //get ToDOItem description from mEditText
        String mToDoItemDescription = mEditText.getText().toString();

        if(mToDoItemDescription.trim().isEmpty()){
            //if this description is null, popup a toast to notify the user
            Toast.makeText(getApplicationContext(), "No ToDo to save", LENGTH_SHORT).show();
        }
        else{

            //else create a new intent to pass available info about ToDoItem back to MainActivity
            Intent mToDoItemIntent = new Intent();
            mToDoItemIntent.putExtra(EXTRA_DESCRIPTION, mToDoItemDescription);
            mToDoItemIntent.putExtra(EXTRA_DATE, (mDate!=null)?mDate.getTime():null);
            setResult(RESULT_OK, mToDoItemIntent);

            //finish current activity
            finish();
        }
        Log.d(TAG, "saveToDoItem() completed.");
    }

}
