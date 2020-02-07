package co.nexus.votingapp.Student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.nexus.votingapp.R;

public class StudentHome extends AppCompatActivity {
    private Button buttonFeePayment, buttonVoteCandidate;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private final String TAG = "StudentHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        initToolbar();

        buttonFeePayment = findViewById(R.id.buttonUnionFeePayment);
        buttonVoteCandidate = findViewById(R.id.buttonVoteCandidate);

        buttonVoteCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonFeePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Student");
    }
}
