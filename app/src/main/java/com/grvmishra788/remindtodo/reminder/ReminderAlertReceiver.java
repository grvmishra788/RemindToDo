package com.grvmishra788.remindtodo.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.grvmishra788.remindtodo.add_edit_todo.AddOrEditToDoItemActivity.EXTRA_DESCRIPTION;

public class ReminderAlertReceiver extends BroadcastReceiver {

    //contants
    private static final String TAG = ReminderAlertReceiver.class.getName();     //constant Class TAG

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OnReceive() called!");
        ReminderNotificationHelper reminderNotificationHelper = new ReminderNotificationHelper(context);
        String mToDoItemDescription = intent.getStringExtra(EXTRA_DESCRIPTION);
        NotificationCompat.Builder nb = reminderNotificationHelper.getChannelNotification(mToDoItemDescription);
        reminderNotificationHelper.getManager().notify(1, nb.build());
        Log.d(TAG, "OnReceive() completed!");
    }
}
