package co.nexus.votingapp.Helpers;

import java.util.Calendar;
import java.util.Date;

public class Notification {
    public String notification;
    public long time;

    public Notification(String notification, long time) {
        this.notification = notification;
        this.time = time;
    }

    public Notification() {}


    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static long getCurrentTime(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.getTime();
    }
}
