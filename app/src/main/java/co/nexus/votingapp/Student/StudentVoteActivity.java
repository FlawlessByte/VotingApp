package co.nexus.votingapp.Student;

import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.AppCompatActivity;

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

import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.R;

public class StudentVoteActivity extends AppCompatActivity {
    private Button buttonStudentMaleCandidate, buttonStudentFemaleCadidate;
    private final String TAG = "StudentVoteActivity";
    private boolean isStudentVoteEligible = false;
    private ProgressDialog progressDialog;
    private String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_vote);

        progressDialog = showProgressDialog();
        checkVoteEligible();

        buttonStudentMaleCandidate = findViewById(R.id.buttonStudentMaleCandidate);
        buttonStudentFemaleCadidate = findViewById(R.id.buttonStudentFemaleCandidate);

        buttonStudentMaleCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Male Candidate button clicked");
                if(checkVoteEligibilityForGender("M"))
                    startActivity(new Intent(StudentVoteActivity.this, VoteActivity.class).putExtra("gender", "M"));
                else
                    Toast.makeText(StudentVoteActivity.this, "You are not eligible to vote", Toast.LENGTH_SHORT).show();
            }
        });


        buttonStudentFemaleCadidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Female candidate button clicked");
                if(checkVoteEligibilityForGender("F"))
                    startActivity(new Intent(StudentVoteActivity.this, VoteActivity.class).putExtra("gender", "F"));
                else
                    Toast.makeText(StudentVoteActivity.this, "You are not eligible to vote", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkVoteEligible(){

        uid = FirebaseAuth.getInstance().getUid();
        Log.d(TAG, "UID : "+uid);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("students").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                Student student = dataSnapshot.getValue(Student.class);
                isStudentVoteEligible = student.isVoteEligible();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });
    }


    private boolean checkVoteEligibilityForGender(String gender){
        String val = "";
        switch (gender){
            case "M": {
                val = "vote_male_candidate";
                break;
            }
            case "F": {
                val = "vote_female_candidate";
                break;
            }
            default:
                break;
        }

        if(isStudentVoteEligible){
            SharedPreferences sharedPref = getSharedPreferences(uid, MODE_PRIVATE);
            boolean isEligible = sharedPref.getBoolean(val,true);
            return isEligible;
        }
        else{
            Log.d(TAG, "Not eligible to vote");
            return false;
        }
    }



    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

    public void backButtonPressed(View view) {
        onBackPressed();
    }
}
