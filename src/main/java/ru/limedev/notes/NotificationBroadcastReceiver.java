package ru.limedev.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.zip.DataFormatException;

import ru.limedev.notes.model.NotificationBuilder;

import static ru.limedev.notes.model.Constants.NOTIFICATION_EXTRA_ID;
import static ru.limedev.notes.model.Constants.NOTIFICATION_EXTRA_NAME;
import static ru.limedev.notes.model.Constants.NOTIFICATION_EXTRA_TEXT;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int notificationId = intent.getIntExtra(NOTIFICATION_EXTRA_ID, 0);
            String notificationName = intent.getStringExtra(NOTIFICATION_EXTRA_NAME);
            String notificationText = intent.getStringExtra(NOTIFICATION_EXTRA_TEXT);
            NotificationBuilder notificationBuilder = new NotificationBuilder(context,
                    notificationId, notificationName, notificationText);
            notificationBuilder.buildPressableNotification(MainActivity.class);
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
    }
}
