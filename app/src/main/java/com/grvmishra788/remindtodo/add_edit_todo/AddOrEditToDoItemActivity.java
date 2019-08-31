package com.grvmishra788.remindtodo.add_edit_todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.grvmishra788.remindtodo.MainActivity;
import com.grvmishra788.remindtodo.R;
import com.grvmishra788.remindtodo.basic.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class AddOrEditToDoItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //constant strings
    private static final String TAG = AddOrEditToDoItemActivity.class.getName();  //constant Class TAG
    public static final String EXTRA_DESCRIPTION = "com.grvmishra788.remindtodo.add_todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.grvmishra788.remindtodo.add_todo.EXTRA_DATE";
    public static final String EXTRA_REMINDER = "com.grvmishra788.remindtodo.add_edit_todo.EXTRA_REMINDER";
    public static final String EXTRA_POSITION = "com.grvmishra788.remindtodo.add_edit_todo.EXTRA_POSITION";
    public static final String EXTRA_TASK_FINISHED = "com.grvmishra788.remindtodo.add_edit_todo.EXTRA_TASK_FINISHED";
    public static final String DATE_FORMAT_ONLY_TIME_1 = "hh:mm a"; //Date format string to show just time - in 12 hour view
    public static final String DATE_FORMAT_ONLY_TIME_2 = "HH:mm"; //Date format string to show just time - in  24 hour view
    public static final String DATE_FORMAT_DAY_AND_DATE = "EEE - MMM dd, yyyy"; //Date format string to show Day and Date

    //constant request code for Intent started by mSpeechButton
    public static final int VOICE_INPUT = 1001;

    //EditText variable
    private EditText mEditText;

    //TextView variables
    private TextView mEditDate, mEditTime;

    //Reminder switch
    private Switch mReminderSwitch, mTaskFinishedSwitch;

    //ImageButton variables
    private ImageButton mDateButton, mTimeButton, mSpeechButton;

    //DAte variables
    Date mDate;

    //Variable to store the intent which started the activity
    Intent mActivityStartingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called for " + TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);

        //init mDate with EOD
        mDate = Utilities.getEndOfDay();

        //init EditText variables
        mEditText = (EditText) findViewById(R.id.editText);

        //init TextView variables
        mEditDate = (TextView) findViewById(R.id.editDate);
        mEditTime = (TextView) findViewById(R.id.editTime);

        //init switch variables
        mReminderSwitch = (Switch) findViewById(R.id.reminderSwitch);
        mTaskFinishedSwitch = (Switch) findViewById(R.id.taskFinishedSwitch);

        //init Speech Button variable & set its onClick Listener
        mSpeechButton = (ImageButton) findViewById(R.id.speechButton);
        mSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickListener called for mSpeechButton");
                int REQUEST_CODE = 1;
                String DIALOG_TEXT = "Speak now ... ";
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, DIALOG_TEXT);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, REQUEST_CODE);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                startActivityForResult(intent, VOICE_INPUT);
                Log.d(TAG, "onClickListener finished for mSpeechButton");
            }
        });

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

        //init Time Button variable & set its onClick Listener
        mTimeButton = (ImageButton) findViewById(R.id.saveTime);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickListener called for mTimeButton");
                DialogFragment mTimePicker = new TimePickerFragment();
                mTimePicker.show(getSupportFragmentManager(), "Time Picker Dialog");
                Log.d(TAG, "onClickListener finished for mTimeButton");
            }
        });

        //init reminder switch

        //init close activity button on left hand top side of activity
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_activity);

        //catch the intent which started the activity
        mActivityStartingIntent = getIntent();

        //check if this intent was sent to Edit or Add ToDoItem and set title accordingly
        if (mActivityStartingIntent.hasExtra(EXTRA_POSITION)) {
            Log.d(TAG, "Started setting default fields as activity started by EDIT_TO_DO_ITEM Intent");
            //incase intent was sent to Edit ToDoItem

            //set Title of activity as Edit ToDoItem
            setTitle("Edit ToDo Item");

            //set mEditText with existing ToDoITem description
            mEditText.setText(mActivityStartingIntent.getStringExtra(EXTRA_DESCRIPTION));

            //set mEditDate with existing ToDoITem date
            mDate = new Date(mActivityStartingIntent.getExtras().getLong(EXTRA_DATE));
            SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_DAY_AND_DATE);
            String currentDateTimeString = sdf.format(mDate);
            mEditDate.setText(currentDateTimeString);

            //change visibility of time fields
            mEditTime.setVisibility(View.VISIBLE);
            mTimeButton.setVisibility(View.VISIBLE);

            //change visibility of switches
            mReminderSwitch.setVisibility(View.VISIBLE);
            mTaskFinishedSwitch.setVisibility(View.VISIBLE);

            //set mEditTime with existing ToDoITem time
            sdf=new SimpleDateFormat(MainActivity.getDefaultDateDisplayFormat());
            currentDateTimeString = sdf.format(mDate);
            mEditTime.setText(currentDateTimeString);
            Log.d(TAG, "Completed setting default fields as activity started to Edit ToDo Item Intent");

            //turn on mReminderSwitch if ToDoItem has a reminder
            mReminderSwitch.setChecked(mActivityStartingIntent.getExtras().getBoolean(EXTRA_REMINDER));

            //turn on mTaskFinishedSwitch if ToDoItem has been finished
            mTaskFinishedSwitch.setChecked(mActivityStartingIntent.getExtras().getBoolean(EXTRA_TASK_FINISHED));
        }
        else {
            if(mActivityStartingIntent.hasExtra(EXTRA_DESCRIPTION)){
                //incase intent was sent to Add ToDoItem from clipboard
                //set Title of activity as Add ToDoItem
                setTitle("Add ToDo Item");
                //set mEditText with existing ToDoITem description
                mEditText.setText(mActivityStartingIntent.getStringExtra(EXTRA_DESCRIPTION));
                Log.d(TAG, "Completed setting title & description field as activity started by ADD_TO_DO_ITEM_FROM_CLIPBOARD Intent");
            } else {
                //incase intent was sent to Add ToDoItem
                //set Title of activity as Add ToDoItem
                setTitle("Add ToDo Item");
                Log.d(TAG, "Completed setting title as activity started by ADD_TO_DO_ITEM_FROM_CLIPBOARD Intent");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        Log.d(TAG, "onCreateOptionsMenu() called for AddOrEditToDoItemActivity menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_todoitem_menu, menu);
        if(mActivityStartingIntent.hasExtra(EXTRA_POSITION)){
            //if intent was sent to edit toDoitem, add delete button in menu
            menu.findItem(R.id.deleteToDoItem).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected() called for AddOrEditToDoItemActivity menu");
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.saveToDoItem:
                Log.d(TAG, "saveToDoItem selected for AddOrEditToDoItemActivity menu");
                saveToDoItem();
                return true;
            case R.id.deleteToDoItem:
                Log.d(TAG, "deleteToDoItem selected for AddOrEditToDoItemActivity menu");
                deleteToDoItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //function to send intent & delete ToDoitem if delete button is pressed in menu
    private void deleteToDoItem() {
        Log.d(TAG, "deleteToDoItem() called.");
        Intent deleteToDoItemIntent = new Intent();
        int position = mActivityStartingIntent.getIntExtra(EXTRA_POSITION, -1);
        if(position!=-1){
            deleteToDoItemIntent.putExtra(EXTRA_POSITION, position);
        }
        setResult(RESULT_OK, deleteToDoItemIntent);
        //finish current activity
        finish();
        Log.d(TAG, "deleteToDoItem() completed.");
    }

    //function to send intent & save ToDoitem if save button is pressed in menu
    private void saveToDoItem() {
        Log.d(TAG, "saveToDoItem() called.");

        //get ToDOItem description from mEditText
        String mToDoItemDescription = mEditText.getText().toString();

        if (mToDoItemDescription.trim().isEmpty()) {
            //if this description is null, popup a toast to notify the user
            Toast.makeText(getApplicationContext(), "No ToDo to save", LENGTH_SHORT).show();
        } else {

            //by default item's date is set to be the EOD of ongoing day, if user didnt specify it
            if (mDate == null) mDate = Utilities.getEndOfDay();

            //else create a new intent to pass available info about ToDoItem back to MainFragment
            Intent mToDoItemIntent = new Intent();
            mToDoItemIntent.putExtra(EXTRA_DESCRIPTION, mToDoItemDescription);
            mToDoItemIntent.putExtra(EXTRA_DATE, mDate.getTime());
            mToDoItemIntent.putExtra(EXTRA_REMINDER, mReminderSwitch.isChecked());
            mToDoItemIntent.putExtra(EXTRA_TASK_FINISHED, mTaskFinishedSwitch.isChecked());
            setResult(RESULT_OK, mToDoItemIntent);

            //put this position into intent only if it has been started by an Edit ToDoItem Intent
            int position = mActivityStartingIntent.getIntExtra(EXTRA_POSITION, -1);
            if(position!=-1){
                mToDoItemIntent.putExtra(EXTRA_POSITION, position);
            }

            //finish current activity
            finish();
        }
        Log.d(TAG, "saveToDoItem() completed.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult called for requestCode = " + Integer.toString(requestCode));
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> speech;
        if (requestCode == VOICE_INPUT && resultCode == RESULT_OK) {
            speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mEditText.setText(speech.get(0));
            Log.d(TAG, "Speech recognition success and String put into mEditText");
        }
    }

    //implement DatePickerDialog.OnDateSetListener
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Log.d(TAG, "OnDateSetListener() called");

        //get year, month and day from calendar instance && hours, minutes from earlier existing mDate
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(mDate);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);

        //create date object
        mDate = mCalendar.getTime();

        //convert date to string & display in text view
        SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT_DAY_AND_DATE);
        String currentDateTimeString = sdf.format(mDate);
        mEditDate.setText(currentDateTimeString);

        //change visibility of time fields
        mEditTime.setVisibility(View.VISIBLE);
        mTimeButton.setVisibility(View.VISIBLE);

        //change visibility of reminder switch
        mReminderSwitch.setVisibility(View.VISIBLE);

        Log.d(TAG, "OnDateSetListener() call completed");
    }

    //implement TimePickerDialog.OnDateSetListener
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Log.d(TAG, "OnTimeSetListener() call started");
        mDate.setHours(hour);
        mDate.setMinutes(minute);
        SimpleDateFormat sdf=new SimpleDateFormat(MainActivity.getDefaultDateDisplayFormat());
        String currentDateTimeString = sdf.format(mDate);
        mEditTime.setText(currentDateTimeString);
        Log.d(TAG, "OnTimeSetListener() call completed");
    }
}
