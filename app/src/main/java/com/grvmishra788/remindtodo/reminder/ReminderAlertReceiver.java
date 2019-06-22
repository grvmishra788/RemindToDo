package com.grvmishra788.remindtodo.reminder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.grvmishra788.remindtodo.MainActivity;
import com.grvmishra788.remindtodo.MainFragment;

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
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        nb.setContentIntent(contentIntent);
        reminderNotificationHelper.getManager().notify(1, nb.build());
        Log.d(TAG, "OnReceive() completed!");
    }
}
