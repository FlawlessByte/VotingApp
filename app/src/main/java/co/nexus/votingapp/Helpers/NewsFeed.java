package co.nexus.votingapp.Helpers;

import java.util.Calendar;
import java.util.Date;

public class NewsFeed {
    public String news;
    public long time;

    public NewsFeed(String news, long time) {
        this.news = news;
        this.time = time;
    }

    public NewsFeed() {}

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
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
