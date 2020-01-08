package com.example.justdoit.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("Title");
        //int taskId = intent.getIntExtra("Id", 0);
        long taskId = intent.getLongExtra("TaskID", 0L);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(title, taskId);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
