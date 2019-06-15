package com.grvmishra788.remindtodo.reminder;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.grvmishra788.remindtodo.R;


public class ReminderNotificationHelper extends ContextWrapper {

    //contants
    private static final String TAG = ReminderNotificationHelper.class.getName();     //constant Class TAG

    public static final String channelID = "com.grvmishra788.remindtodo.reminder.channelID";
    public static final String channelName = "com.grvmishra788.remindtodo.reminder.channelName";

    private NotificationManager mManager;

    public ReminderNotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        Log.d(TAG, "getManager() called!");
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d(TAG, "mManager() init success!");
        }
        Log.d(TAG, "getManager() completed!");
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String mToDoItemDescription) {
        Log.d(TAG, "getChannelNotification() called & completed!");
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(mToDoItemDescription)
                .setContentText("Overdue!! - This task needs to be completed soon.")
                .setSmallIcon(R.mipmap.ic_launcher_foreground);
    }
}