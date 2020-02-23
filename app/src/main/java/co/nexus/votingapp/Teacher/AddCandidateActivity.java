package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.obsez.android.lib.filechooser.ChooserDialog;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.R;

public class AddCandidateActivity extends AppCompatActivity {
    private ImageButton buttonLeftCandidate, buttonDoneCandidate;
    private TextInputEditText candidateNameEditText, candidateDOBEditText;
    private EditText canndidateDepartmentEditText, candidateYOSEditText, candidatePartyEditText;
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
        candidatePartyEditText = findViewById(R.id.candidatePartyEditText);
        candidateSearch = findViewById(R.id.candidate_search);
        civ = findViewById(R.id.candidateProfImg);


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

        candidatePartyEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Party select button pressed");
                showPartyDialog(v);
            }
        });



//        canndidateDepartmentEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Department Dialog clicked");
//                showDepartmentDialog(v);
//            }
//        });

//        candidateYOSEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Year Of Study clicked");
//                showYearOfStudyDialog(v);
//            }
//        });

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

            }
        });


    }

    private void searchAction(){
        Log.d(TAG, "Search Action");
        if(students.size() != 0){
            int i = 0;
            for(Student stud : students){
                if(stud.getAdmissionNo().equals(candidateSearch.getText().toString())){
                    Log.d(TAG, "Admission no match!");
                    fillTheFields(stud);
                    position = i++;
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

        mRef.child("candidate").child(candidateKey).setValue(candidate);

        return  candidateKey;
    }


    private Candidate getCandidateObject(Student student){
        String party = candidatePartyEditText.getText().toString();
//        int selectedId = candidateGenderRadioGroup.getCheckedRadioButtonId();
//        gender = ((RadioButton)findViewById(selectedId)).getText().toString();

        Candidate candidate = new Candidate(student.getName(), student.getDob(), student.getGender(), student.getDepartment(),
                student.getImgDownloadUrl(), student.getYearOfStudy(), 0, party);

        return candidate;
    }


    private int getInputErrorCount(){
        int errorCount = 0;
        String party = candidatePartyEditText.getText().toString();

        if(party.equals(""))
            errorCount++;


        return errorCount;
    }



    private void showPartyDialog(final View v){
        final String[] array = new String[]{
                "KSU", "SFI", "ABVP"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Party");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


}
