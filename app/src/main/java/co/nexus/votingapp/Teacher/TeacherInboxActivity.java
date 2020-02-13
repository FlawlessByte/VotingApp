package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.Helpers.StudentAdapter;
import co.nexus.votingapp.R;

public class TeacherInboxActivity extends AppCompatActivity {
    private final String TAG = "TeacherInboxActivity";
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private RecyclerView recyclerView;
    private StudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_inbox);

        recyclerView = findViewById(R.id.recyclerViewTeacherInbox);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new StudentAdapter(this, students, keys);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
                keys.clear();
                for(DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                    Student student = singleSnapShot.getValue(Student.class);
                    if(!student.isConfirmed() && !student.isRemoved()){
                        students.add(student);
                        keys.add(singleSnapShot.getKey());
                        Log.d(TAG, "Found student that is not confirmed");
                    }
                }

                Log.d(TAG, "Size : "+students.size());

                doAdapterStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });
    }

    private void doAdapterStuff(){
        mAdapter = new StudentAdapter(this, students, keys);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
