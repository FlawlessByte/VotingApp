package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.obsez.android.lib.filechooser.ChooserDialog;

import org.w3c.dom.Text;

import java.io.File;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.R;

public class AddCandidateActivity extends AppCompatActivity {
    private ImageButton buttonLeftCandidate, buttonDoneCandidate;
    private TextInputEditText candidateNameEditText, candidateDOBEditText;
    private EditText canndidateDepartmentEditText, candidateYOSEditText, candidatePartyEditText;
    private RadioGroup candidateGenderRadioGroup;
    private ImageView candidateProfileImageView;
    private LinearLayout candidatePhotoLinearLayout;
    private TextView candidateImagePathTextView;
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
        candidateImagePathTextView = findViewById(R.id.candidateImagePathTextView);
        candidatePartyEditText = findViewById(R.id.candidatePartyEditText);

        candidatePartyEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Party select button pressed");
                showPartyDialog(v);
            }
        });


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
                onBackPressed();

            }
        });

        buttonDoneCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Done Button Clicked");
                if(getInputErrorCount() == 0){
                    //No errors
                    String key = addCandidateToDB();
                    saveCandidatePhoto(key);
                    Toast.makeText(getApplicationContext(), "Added Candidate!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }
        });


    }

    private void saveCandidatePhoto(String key){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");

        String path = candidateImagePathTextView.getText().toString();
//        String filename = path.substring(path.lastIndexOf("/")+1);
//        StorageReference profRef = imagesRef.child(filename);


        Uri file = Uri.fromFile(new File(path));
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        String modifiedName = key+"."+extension;
        Log.d(TAG, "FIle Name : "+modifiedName);
        StorageReference profRef = imagesRef.child(modifiedName);

        UploadTask uploadTask = profRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "Failed to upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d(TAG, "Success uploading");
            }
        });


    }

    private String addCandidateToDB(){
        Candidate candidate = getCandidateObject();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        String candidateKey = mRef.child("candidate").push().getKey();

        mRef.child("candidate").child(candidateKey).setValue(candidate);

        return  candidateKey;
    }


    private Candidate getCandidateObject(){
        String name, dob, dept, gender, imgPath, party;
        int yos;
        name = candidateNameEditText.getText().toString();
        dob = candidateDOBEditText.getText().toString();
        dept = canndidateDepartmentEditText.getText().toString();
        party = candidatePartyEditText.getText().toString();
        yos = Integer.parseInt(candidateYOSEditText.getText().toString());
        int selectedId = candidateGenderRadioGroup.getCheckedRadioButtonId();
        gender = ((RadioButton)findViewById(selectedId)).getText().toString();
        imgPath = candidateImagePathTextView.getText().toString();

        Candidate candidate = new Candidate(name, dob, gender, dept, imgPath, yos, 0, party);

        return candidate;
    }


    private int getInputErrorCount(){
        int errorCount = 0;

        String yos = candidateYOSEditText.getText().toString();
        String dept = canndidateDepartmentEditText.getText().toString();
        String party = candidatePartyEditText.getText().toString();

        if(TextUtils.isEmpty(candidateNameEditText.getText()) || TextUtils.isEmpty(candidateDOBEditText.getText()) ||
                TextUtils.isEmpty(yos) || TextUtils.isEmpty(dept)){
            errorCount++;
        }

        if(yos.equals("Year of Study") || dept.equals("Department") || party.equals("Candidate Party"))
            errorCount++;


        return errorCount;
    }


    private void doProfileImageSelectionStuff(){
        new ChooserDialog(AddCandidateActivity.this)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        Toast.makeText(AddCandidateActivity.this, "FILE: " + path, Toast.LENGTH_SHORT).show();
                        candidateImagePathTextView.setText(path);
                        candidateProfileImageView.setImageBitmap(BitmapFactory.decodeFile(path));
                    }
                })
                // to handle the back key pressed or clicked outside the dialog:
                .withOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        Log.d("CANCEL", "CANCEL");
                        dialog.cancel(); // MUST have
                    }
                })
                .build()
                .show();

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
