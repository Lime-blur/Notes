package ru.limedev.notes.model.pojo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ru.limedev.notes.NotificationBroadcastReceiver;
import ru.limedev.notes.model.exceptions.ParseDataException;

import static ru.limedev.notes.model.Constants.NOTIFICATION_EXTRA_ID;
import static ru.limedev.notes.model.Constants.NOTIFICATION_EXTRA_NAME;
import static ru.limedev.notes.model.Constants.NOTIFICATION_EXTRA_TEXT;
import static ru.limedev.notes.model.Utilities.checkStrings;

public class Notification extends Datetime {

    private final int id;
    private final String name;
    private final String text;

    private static final int REQUEST_CODE = 0;

    public Notification(String date, String time, int id, String name, String text)
            throws ParseDataException {
        super(date, time);
        if (checkStrings(name, text)) {
            this.id = id;
            this.name = name;
            this.text = text;
        } else {
            throw new ParseDataException();
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void createNotification(Context context) throws ParseDataException {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra(NOTIFICATION_EXTRA_ID, getId());
        intent.putExtra(NOTIFICATION_EXTRA_NAME, getName());
        intent.putExtra(NOTIFICATION_EXTRA_TEXT, getText());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getCalendarDatetime().getTimeInMillis(),
                pendingIntent);
    }
}
