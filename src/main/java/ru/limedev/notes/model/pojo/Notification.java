package ru.limedev.notes.model.pojo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ru.limedev.notes.NotificationBroadcastReceiver;
import ru.limedev.notes.model.exceptions.ParseDateException;

public class Notification extends Datetime {

    public Notification(String date, String time) throws ParseDateException {
        super(date, time);
    }

    public void createNotification(Context context) throws ParseDateException {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getCalendarDatetime().getTimeInMillis(),
                pendingIntent);
    }
}
