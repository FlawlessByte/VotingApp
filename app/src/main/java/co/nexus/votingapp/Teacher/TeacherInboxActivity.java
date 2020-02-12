package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.R;

public class TeacherInboxActivity extends AppCompatActivity {
    private final String TAG = "TeacherInboxActivity";
    private ArrayList<Student> students = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_inbox);

        getData();


    }


    private void getData(){
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                students.clear();
                for(DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                    Student student = singleSnapShot.getValue(Student.class);
                    if(student.isConfirmed()){
                        students.add(student);
                    }
                }

                doAdapterStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });
    }

    private void doAdapterStuff(){
        
    }
}
