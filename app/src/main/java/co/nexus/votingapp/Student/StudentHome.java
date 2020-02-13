package co.nexus.votingapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.R;

public class StudentHome extends AppCompatActivity {
    private Button buttonFeePayment, buttonVoteCandidate;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private final String TAG = "StudentHome";
    private boolean isStudentAuthorized = false;
    private ProgressDialog progressDialog;
    private String uid;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        initToolbar();

        initialiseSharedPrefs();

        progressDialog = showProgressDialog();

        uid = FirebaseAuth.getInstance().getUid();
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("students").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                Student student = dataSnapshot.getValue(Student.class);
                isStudentAuthorized = student.isConfirmed();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
                progressDialog.dismiss();
            }
        });



        buttonFeePayment = findViewById(R.id.buttonUnionFeePayment);
        buttonVoteCandidate = findViewById(R.id.buttonVoteCandidate);

        buttonVoteCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudentAuthorized)
                    startActivity(new Intent(StudentHome.this, StudentVoteActivity.class));
                else
                    Toast.makeText(StudentHome.this, "Your account is not yet confirmed by the teacher!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonFeePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudentAuthorized)
                    startActivity(new Intent(StudentHome.this, StudentPaymentActivity.class));
                else
                    Toast.makeText(StudentHome.this, "Your account is not yet confirmed by the teacher!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

    private void initialiseSharedPrefs(){
        SharedPreferences sharedPref = getSharedPreferences(uid, MODE_PRIVATE);
        boolean isMaleVoteEligible = sharedPref.getBoolean(Constants.male_sharedpref, true);
        boolean isFemaleVoteEligible = sharedPref.getBoolean(Constants.female_sharedpref, true);

        if(!isMaleVoteEligible && !isFemaleVoteEligible){
            Log.d(TAG, "Student done with voting");
            mRef.child("students").child(uid).child("voteEligible").setValue(false);
        }
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
