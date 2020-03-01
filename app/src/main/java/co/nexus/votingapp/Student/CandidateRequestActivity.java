package co.nexus.votingapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.R;
import co.nexus.votingapp.Teacher.AddCandidateActivity;

public class CandidateRequestActivity extends AppCompatActivity {
    private ImageButton buttonLeftCandidate, buttonDoneCandidate;
    private EditText canndidateDepartmentEditText, candidateYOSEditText, candidateDescEditText, candidateNameEditText, candidateDOBEditText;
    private Student student;
    private RadioButton maleRadioButton, femaleRadioButton, otherRadioButton;
    private final String TAG = "CandidateRequestAct";
    private CircularImageView civ;
    private ProgressDialog progressDialog;
    private String key = FirebaseAuth.getInstance().getUid();
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private boolean isDataReady = false;
    private NestedScrollView detailsLayouNSV;
    private TextView statusTextView;
    private LinearLayout statusLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_request);

        progressDialog = showProgressDialog();
        progressDialog.show();

        student = (Student) getIntent().getSerializableExtra("student");


        detailsLayouNSV = findViewById(R.id.candidateRequestNSV);
        buttonDoneCandidate = findViewById(R.id.buttonDoneCandidate);
        buttonLeftCandidate = findViewById(R.id.buttonLeftCandidate);
        candidateNameEditText = findViewById(R.id.candidateNameEditText);
        candidateDOBEditText = findViewById(R.id.candidateDOBEditText);
        canndidateDepartmentEditText = findViewById(R.id.candidateDepartmentEditText);
        candidateYOSEditText = findViewById(R.id.candidateYOSEditText);
        candidateDescEditText = findViewById(R.id.candidateDescriptionEditText);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        otherRadioButton = findViewById(R.id.otherRadioButton);
        civ = findViewById(R.id.candidateProfImg);
        statusTextView = findViewById(R.id.statusTextView);
        statusLinearLayout = findViewById(R.id.statusLayout);


        mRef.child("candidate").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isDataReady = true;
                progressDialog.dismiss();
                if(dataSnapshot.hasChildren()){
                    //Request is already given
                    statusLinearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(CandidateRequestActivity.this, "You have already requested to participate in election!", Toast.LENGTH_SHORT).show();
                    Candidate candidate = dataSnapshot.getValue(Candidate.class);

                    if(candidate.isReviewed()){
                        //Candidate request is reviewed
                        if(candidate.isConfirmed()){
                            //Request confirmed
                            statusTextView.setText("Confirmed");
                        }
                        else{
                            //Request rejected
                            statusTextView.setText("Rejected");
                        }
                    }
                    else{
                        //Request not reviewed
                        // Msg to show
                        statusTextView.setText("Not yet reviewed");
                    }


                }
                else{
                    //Not requested yet, student can request
                    detailsLayouNSV.setVisibility(View.VISIBLE);
                    setuptheFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });

        buttonLeftCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Left Button clicked");
                onBackPressed();
            }
        });

    }

    private void setuptheFields(){
        //update fields
        Glide.with(this).load(student.getImgDownloadUrl()).into(civ);
        candidateNameEditText.setText(student.getName());
        candidateDOBEditText.setText(student.getDob());
        canndidateDepartmentEditText.setText(student.getDepartment());
        candidateYOSEditText.setText(""+student.getYearOfStudy());
        switch (student.getGender()){
            case "M":{
                maleRadioButton.setChecked(true);
                break;
            }
            case "F":{
                femaleRadioButton.setChecked(true);
                break;
            }
            case "O":{
                otherRadioButton.setChecked(true);
                break;
            }
            default:{
                break;
            }
        }

        buttonDoneCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Done Button Clicked");
                if(isDataReady) {
                    String desc = candidateDescEditText.getText().toString();
                    if (!TextUtils.isEmpty(desc)) {
                        //No errors
                        addCandidateToDB();
                        Toast.makeText(getApplicationContext(), "Added Candidate!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a description!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addCandidateToDB() {

        Candidate candidate = new Candidate(student.getName(), student.getDob(), student.getGender(), student.getDepartment(),
                student.getImgDownloadUrl(), student.getYearOfStudy(), 0, candidateDescEditText.getText().toString(), false, false);

        progressDialog.show();
        mRef.child("candidate").child(key).setValue(candidate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Task success!");
                    progressDialog.dismiss();
                    Toast.makeText(CandidateRequestActivity.this, "Request successfully submitted!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(CandidateRequestActivity.this, "Sorry! Some error occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        return dialog;
    }
}
