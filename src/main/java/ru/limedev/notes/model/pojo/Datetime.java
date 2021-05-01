package ru.limedev.notes.model.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.limedev.notes.model.exceptions.ParseDateException;

import static ru.limedev.notes.model.Utilities.checkStrings;

public class Datetime {

    private final Date date;
    private final Date time;

    private final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat DB_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private final int BEFORE = -1;
    private final int NOW = 0;
    private final int AFTER = 1;

    public Datetime(String date, String time) throws ParseDateException {
        if (checkStrings(date, time)) {
            try {
                this.date = DATE_FORMAT.parse(date);
                this.time = TIME_FORMAT.parse(time);
            } catch (ParseException ex) {
                throw new ParseDateException();
            }
        } else {
            throw new ParseDateException();
        }
    }

    public int isFutureDate() throws ParseDateException {
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
            throw new ParseDateException();
        }
    }

    public int isFutureTime() throws ParseDateException {
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
            throw new ParseDateException();
        }
    }

    public boolean isFutureDatetime() throws ParseDateException {
        if (isFutureDate() == AFTER) {
            return true;
        } else if (isFutureDate() == NOW) {
            return isFutureTime() == NOW || isFutureTime() == AFTER;
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

    public String getStringTime() {
        return TIME_FORMAT.format(time);
    }

    public Date getTime() {
        return time;
    }
}
