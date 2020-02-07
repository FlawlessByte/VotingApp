package co.nexus.votingapp.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import co.nexus.votingapp.R;

public class TeacherHome extends AppCompatActivity {
    private Button buttonAddCandidate, buttonTeacherInbox, buttonTeacherLogOut;
    private final String TAG = "TeacherHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        buttonAddCandidate = findViewById(R.id.buttonAddCandidate);
        buttonTeacherInbox = findViewById(R.id.buttonTeacherInbox);
        buttonTeacherLogOut = findViewById(R.id.buttonTeacherSignOut);

        buttonAddCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Candidate clicked!");
                startActivity(new Intent(TeacherHome.this, AddCandidateActivity.class));
            }
        });

        buttonTeacherInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Inbox clicked!");
            }
        });

        buttonTeacherLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Sign Out clicked!");
            }
        });

    }
}
