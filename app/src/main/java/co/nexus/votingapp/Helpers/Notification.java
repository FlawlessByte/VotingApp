package co.nexus.votingapp.Helpers;

import java.util.Calendar;
import java.util.Date;

public class Notification {
    public String content, title;
    public long time;

    public Notification(String title, String content, long time) {
        this.content = content;
        this.title = title;
        this.time = time;
    }

    public Notification() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
