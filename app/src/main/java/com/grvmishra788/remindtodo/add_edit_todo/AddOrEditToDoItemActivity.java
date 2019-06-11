package com.grvmishra788.remindtodo.add_edit_todo;

import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.grvmishra788.remindtodo.R;
import com.grvmishra788.remindtodo.basic.Utilities;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class AddOrEditToDoItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //constant strings
    private static final String TAG = AddOrEditToDoItemActivity.class.getName();  //constant Class TAG
    public static final String EXTRA_DESCRIPTION = "com.grvmishra788.remindtodo.add_todo.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.grvmishra788.remindtodo.add_todo.EXTRA_DATE";
    public static final String EXTRA_POSITION = "com.grvmishra788.remindtodo.add_edit_todo.EXTRA_POSITION";
    public static final int VOICE_INPUT = 1001;

    //EditText variable
    private EditText mEditText;

    //TextView variable
    private TextView mEditDate;

    //ImageButton variables
    private ImageButton mDateButton, mSpeechButton;

    //DAte variables
    Date mDate;

    //Variable to store the intent which started the activity
    Intent mActivityStartingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called for " + TAG);
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


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_activity);

        //catch the intent which started the activity
        mActivityStartingIntent = getIntent();

        //check if this intent was sent to Edit or Add ToDoItem and set title accordingly
        if (mActivityStartingIntent.hasExtra(EXTRA_POSITION)) {
            setTitle("Edit ToDo Item");
            //incase intent was sent to Edit ToDoItem, set textviews with existing values
            mEditText.setText(mActivityStartingIntent.getStringExtra(EXTRA_DESCRIPTION));
            Date mExistingDate = new Date(mActivityStartingIntent.getExtras().getLong(EXTRA_DATE));
            String mDateString = DateFormat.getDateInstance(DateFormat.FULL).format(mExistingDate);
            mEditDate.setText(mDateString);
        } else {
            setTitle("Add ToDo Item");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        Log.d(TAG, "onCreateOptionsMenu() called for AddOrEditToDoItemActivity menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_todoitem_menu, menu);
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

            //else create a new intent to pass available info about ToDoItem back to MainActivity
            Intent mToDoItemIntent = new Intent();
            mToDoItemIntent.putExtra(EXTRA_DESCRIPTION, mToDoItemDescription);
            mToDoItemIntent.putExtra(EXTRA_DATE, mDate.getTime());
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
        }
    }

}
