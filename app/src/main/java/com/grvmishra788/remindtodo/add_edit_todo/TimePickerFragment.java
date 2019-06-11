package com.grvmishra788.remindtodo.add_edit_todo;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private static final String TAG = TimePickerFragment.class.getName();     //constant Class TAG

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateDialog() called");
        Calendar mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(mCalendar.HOUR);
        int minute = mCalendar.get(mCalendar.MINUTE);
        Dialog mDialog = new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)getActivity(), hour, minute, false);
        Log.d(TAG, "OnCreateDialog() call completed");
        return mDialog;
    }
}