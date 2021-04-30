package ru.limedev.notes.model.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.limedev.notes.model.exceptions.ParseStringException;

import static ru.limedev.notes.model.Utilities.checkStrings;

public class Datetime {

    private final String date;
    private final String time;

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    private final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public Datetime(String date, String time) throws ParseStringException {
        if (checkStrings(date, time)) {
            this.date = date;
            this.time = time;
        } else {
            throw new ParseStringException();
        }
    }

    public String getDate() throws ParseStringException {
        try {
            return DB_DATE_FORMAT.format(DATE_FORMAT.parse(date));
        } catch (ParseException ex) {
            throw new ParseStringException();
        }
    }

    public String getTime() throws ParseStringException {
        try {
            TIME_FORMAT.parse(time);
            return time;
        } catch (ParseException ex) {
            throw new ParseStringException();
        }
    }
}
