package ru.limedev.notes.model.parcels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class NotesListItem implements Parcelable {

    private final long id;
    private String name;
    private String text;
    private String date;
    private String time;
    private final int notificationId;
    private double ltd;
    private double lgd;

    public NotesListItem(long id, String name, String text, String date, String time,
                         int notificationId, double ltd, double lgd) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;
        this.time = time;
        this.notificationId = notificationId;
        this.ltd = ltd;
        this.lgd = lgd;
    }

    public static final Creator<NotesListItem> CREATOR = new Creator<NotesListItem>() {
        @Override
        public NotesListItem createFromParcel(Parcel source) {
            long id = source.readLong();
            String name = source.readString();
            String text = source.readString();
            String date = source.readString();
            String time = source.readString();
            int notificationId = source.readInt();
            double ltd = source.readDouble();
            double lgd = source.readDouble();
            return new NotesListItem(id, name, text, date, time, notificationId, ltd, lgd);
        }

        @Override
        public NotesListItem[] newArray(int size) {
            return new NotesListItem[size];
        }
    };

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

    public int getNotificationId() {
        return notificationId;
    }

    public double getLtd() {
        return ltd;
    }

    public void setLtd(double ltd) {
        this.ltd = ltd;
    }

    public double getLgd() {
        return lgd;
    }

    public void setLgd(double lgd) {
        this.lgd = lgd;
    }

    public String getDatetime() {
        return getDate() + " " + getTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(text);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(notificationId);
        dest.writeDouble(ltd);
        dest.writeDouble(lgd);
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
        return Objects.hash(id, name, text, date, time, notificationId, ltd, lgd);
    }

    @Override
    public String toString() {
        return "NotesListItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", notificationId=" + notificationId +
                ", ltd=" + ltd +
                ", lgd=" + lgd +
                '}';
    }
}
