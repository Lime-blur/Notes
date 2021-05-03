package ru.limedev.notes.model.pojo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.zip.DataFormatException;

import ru.limedev.notes.R;

import static ru.limedev.notes.model.Utilities.checkStrings;

public class NotificationBuilder {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private final int id;
    private final String name;
    private final String text;
    private final Context context;

    private static final String NOTIFICATION_CHANNEL_ID = "notes_notification_channel_";
    private static final String CHANNEL_DESCRIPTION = "Notes notification channel ";
    private static final String CHANNEL_NAME = "Notes notification";
    private static final String NOTIFICATION_TICKET = "Check today's notes!";
    private static final int NOTIFICATION_LIGHT_COLOR = -65281;
    private static final long[] VIBRATION_PATTERN = new long[] {0, 200, 200, 300, 0};

    public NotificationBuilder(Context context, int id, String name, String text)
            throws DataFormatException {
        if (context != null && checkStrings(name, text)) {
            this.id = id;
            this.name = name;
            this.text = text;
            this.context = context;
            createNotification(context);
        } else {
            throw new DataFormatException();
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

    public void buildNotification() {
        notificationManager.notify(getId(), notificationBuilder.build());
    }

    public void buildPressableNotification(Class<?> targetActivity) {
        Intent notificationIntent = new Intent(context, targetActivity);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        buildNotification();
    }

    private void createNotification(Context context) {
        if (context != null) {
            notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            checkAndroidOreo(notificationManager);
            notificationBuilder = new NotificationCompat.Builder(context,
                    NOTIFICATION_CHANNEL_ID + getId());
            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_stat_assignment_late)
                    .setTicker(NOTIFICATION_TICKET)
                    .setContentTitle(getName())
                    .setContentText(getText());
        }
    }

    private void checkAndroidOreo(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID + getId(),
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION + getId());
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(NOTIFICATION_LIGHT_COLOR);
            notificationChannel.setVibrationPattern(VIBRATION_PATTERN);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
