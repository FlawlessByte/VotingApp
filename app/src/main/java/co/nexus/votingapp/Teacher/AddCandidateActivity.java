package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;
import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.R;

public class AddCandidateActivity extends AppCompatActivity {
    private ImageButton buttonLeftCandidate, buttonDoneCandidate;
    private ImageView imageViewSearch;
    private TextInputEditText candidateNameEditText, candidateDOBEditText;
    private EditText canndidateDepartmentEditText, candidateYOSEditText, candidateDescEditText;
    private RadioGroup candidateGenderRadioGroup;
    private final String TAG = "AddCandidateActivity";
    private EditText candidateSearch;
    private boolean isCandidateSelected = false;
    private DatabaseReference mRef;
    private ArrayList<Student> students = new ArrayList<>();
    private CircularImageView civ;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    students.add(snap.getValue(Student.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });


        buttonDoneCandidate = findViewById(R.id.buttonDoneCandidate);
        buttonLeftCandidate = findViewById(R.id.buttonLeftCandidate);
        candidateNameEditText = findViewById(R.id.candidateNameEditText);
        candidateDOBEditText = findViewById(R.id.candidateDOBEditText);
        canndidateDepartmentEditText = findViewById(R.id.candidateDepartmentEditText);
        candidateYOSEditText = findViewById(R.id.candidateYOSEditText);
        candidateDescEditText = findViewById(R.id.candidateDescriptionEditText);
        candidateSearch = findViewById(R.id.candidate_search);
        civ = findViewById(R.id.candidateProfImg);
        imageViewSearch = findViewById(R.id.imageViewSearch);



        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search Button clicked");
                searchAction();
            }
        });


        candidateSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "Action search");
                    hideKeyboard();
                    searchAction();
                    return true;
                }
                return false;
            }
        });



        buttonLeftCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Left Button clicked");
                onBackPressed();

            }
        });

        buttonDoneCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Done Button Clicked");
                if(getInputErrorCount() == 0){
                    //No errors
                    String key = addCandidateToDB(students.get(position));
                    Toast.makeText(getApplicationContext(), "Added Candidate!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all the fieldsand try again!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void searchAction(){
        Log.d(TAG, "Search Action");

        if(candidateSearch.getText().toString().equals("")){
            Toast.makeText(AddCandidateActivity.this, "Please enter something and search!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(students.size() != 0){
            for(Student stud : students){
                if(stud.getAdmissionNo().equals(candidateSearch.getText().toString())){
                    Log.d(TAG, "Admission no match!");
                    fillTheFields(stud);
                    position++;
                }
            }
        }
        else{
            Log.d(TAG, "Not yet loaded");
        }

    }


    private void fillTheFields(Student student){
        candidateNameEditText.setText(student.getName());
        candidateDOBEditText.setText(student.getDob());
        canndidateDepartmentEditText.setText(student.getDepartment());
        candidateYOSEditText.setText(String.valueOf(student.getYearOfStudy()));
        Glide.with(AddCandidateActivity.this).load(student.getImgDownloadUrl()).into(civ);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private String addCandidateToDB(Student student){
        Candidate candidate = getCandidateObject(student);

        String candidateKey = mRef.child("candidate").push().getKey();


        Log.d(TAG, "Student Name : "+candidate.getName());

        mRef.child("candidate").child(candidateKey).setValue(candidate);

        return  candidateKey;
    }


    private Candidate getCandidateObject(Student student){
        String desc = candidateDescEditText.getText().toString();
//        int selectedId = candidateGenderRadioGroup.getCheckedRadioButtonId();
//        gender = ((RadioButton)findViewById(selectedId)).getText().toString();

        Candidate candidate = new Candidate(student.getName(), student.getDob(), student.getGender(), student.getDepartment(),
                student.getImgDownloadUrl(), student.getYearOfStudy(), 0, desc, false, false);

        return candidate;
    }


    private int getInputErrorCount(){
        int errorCount = 0;
        String desc = candidateDescEditText.getText().toString();

        if(TextUtils.isEmpty(desc))
            errorCount++;


        return errorCount;
    }



}
