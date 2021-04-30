package ru.limedev.notes.model.beans;

import java.util.Objects;

public class NotesListItem {

    private final long id;
    private String name;
    private String text;
    private String date;
    private String time;

    public NotesListItem(long id, String name, String text, String date, String time) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatetime() {
        return getDate() + " " + getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotesListItem that = (NotesListItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, date, time);
    }

    @Override
    public String toString() {
        return "NotesListItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
