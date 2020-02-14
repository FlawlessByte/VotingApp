package co.nexus.votingapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Notification implements Comparable{
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

    public static void makeNotification(Context context, String title, String msg){
        Notification notification = new Notification(title, msg, getCurrentTime());
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        String key = mRef.child("notifications").push().getKey();
        mRef.child("notifications").child(key).setValue(notification).addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Notification submitted succesfully!" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Date getDateTime(){
        return new Date(getTime());
    }

    @Override
    public int compareTo(Object o) {
        return ((Notification)o).getDateTime().compareTo(getDateTime());
    }
}
