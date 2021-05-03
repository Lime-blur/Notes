package ru.limedev.notes.model.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.limedev.notes.model.exceptions.ParseDataException;

import static ru.limedev.notes.model.Utilities.checkStrings;

public class Datetime {

    private final Date date;
    private final Date time;

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);
    private static final SimpleDateFormat DB_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DATETIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private static final int BEFORE = -1;
    private static final int NOW = 0;
    private static final int AFTER = 1;

    public Datetime(String date, String time) throws ParseDataException {
        if (checkStrings(date, time)) {
            try {
                this.date = DATE_FORMAT.parse(date);
                this.time = TIME_FORMAT.parse(time);
            } catch (ParseException ex) {
                throw new ParseDataException();
            }
        } else {
            throw new ParseDataException();
        }
    }

    public int isFutureDate() throws ParseDataException {
        try {
            Date current = DATE_FORMAT.parse(DATE_FORMAT.format(new Date()));
            if (getDate().equals(current)) {
                return NOW;
            } else if (getDate().after(current)) {
                return AFTER;
            } else {
                return BEFORE;
            }
        } catch (ParseException ex) {
            throw new ParseDataException();
        }
    }

    public int isFutureTime() throws ParseDataException {
        try {
            Date current = TIME_FORMAT.parse(TIME_FORMAT.format(new Date()));
            if (getTime().equals(current)) {
                return NOW;
            } else if (getTime().after(current)) {
                return AFTER;
            } else {
                return BEFORE;
            }
        } catch (ParseException ex) {
            throw new ParseDataException();
        }
    }

    public boolean isFutureDatetime() throws ParseDataException {
        if (isFutureDate() == AFTER) {
            return true;
        } else if (isFutureDate() == NOW) {
            return isFutureTime() == AFTER;
        } else {
            return false;
        }
    }

    public String getStringDate() {
        return DB_DATE_FORMAT.format(date);
    }

    public Date getDate() {
        return date;
    }

    public Calendar getCalendarDatetime() throws ParseDataException {
        try {
            String datetimeString = DB_DATE_FORMAT.format(date) + " " + TIME_FORMAT.format(time) + ":00";
            Date datetime = DATETIME_FORMAT.parse(datetimeString);
            if (datetime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(datetime);
                return calendar;
            } else {
                throw new ParseDataException();
            }
        } catch (ParseException ex) {
            throw new ParseDataException();
        }
    }

    public String getStringTime() {
        return TIME_FORMAT.format(time);
    }

    public Date getTime() {
        return time;
    }
}
