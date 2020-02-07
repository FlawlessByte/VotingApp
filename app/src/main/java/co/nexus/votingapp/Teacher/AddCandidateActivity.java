package co.nexus.votingapp.Teacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;

import co.nexus.votingapp.R;

public class AddCandidateActivity extends AppCompatActivity {
    private ImageButton buttonLeftCandidate, buttonDoneCandidate;
    private TextInputEditText candidateNameEditText, candidateDOBEditText;
    private EditText canndidateDepartmentEditText, candidateYOSEditText;
    private RadioGroup candidateGenderRadioGroup;
    private ImageView candidateProfileImageView;
    private LinearLayout candidatePhotoLinearLayout;
    private final String TAG = "AddCandidateActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);


        buttonDoneCandidate = findViewById(R.id.buttonDoneCandidate);
        buttonLeftCandidate = findViewById(R.id.buttonLeftCandidate);
        candidateNameEditText = findViewById(R.id.candidateNameEditText);
        candidateDOBEditText = findViewById(R.id.candidateDOBEditText);
        canndidateDepartmentEditText = findViewById(R.id.candidateDepartmentEditText);
        candidateYOSEditText = findViewById(R.id.candidateYOSEditText);
        candidateGenderRadioGroup = findViewById(R.id.candidateGenderRadioGroup);
        candidateProfileImageView = findViewById(R.id.candidateProfileImageView);
        candidatePhotoLinearLayout = findViewById(R.id.candiatePhotoLinearLayout);


        candidatePhotoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Select Photo Clicked");
                doProfileImageSelectionStuff();
            }
        });

        canndidateDepartmentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Department Dialog clicked");
                showDepartmentDialog(v);
            }
        });

        candidateYOSEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Year Of Study clicked");
                showYearOfStudyDialog(v);
            }
        });

        buttonLeftCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Left Button clicked");

            }
        });

        buttonDoneCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Done Button Clicked");
            }
        });


    }


    private void doProfileImageSelectionStuff(){

    }



    private void showDepartmentDialog(final View v) {
        final String[] array = new String[]{
                "Mathematics", "Physics", "Chemistry", "BCA", "CS", "Zoology"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Department");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showYearOfStudyDialog(final View v) {
        final String[] array = new String[]{
                "1", "2","3"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Department");
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
