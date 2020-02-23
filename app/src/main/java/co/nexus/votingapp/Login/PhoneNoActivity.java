package co.nexus.votingapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.TimeUnit;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.Helpers.Teacher;
import co.nexus.votingapp.MainActivity;
import co.nexus.votingapp.R;
import co.nexus.votingapp.Student.StudentHome;
import co.nexus.votingapp.Teacher.TeacherHome;

public class PhoneNoActivity extends AppCompatActivity {
    private TextInputEditText numberField, codeField;
    private Button verifyButton, sendOTPButton;
    private String TAG = "PhoneNoActivity";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no);

        String countryCode = "+91";
        String phone = getIntent().getStringExtra("phone");
        category = getIntent().getStringExtra("category");

        numberField = findViewById(R.id.phoneNumberField);
        verifyButton = findViewById(R.id.phoneVerifyContinue);

        numberField.setText(phone);

        codeField = findViewById(R.id.smsCodeField);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        doSmsStuff(countryCode+phone);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OTP verify button pressed");
                String code = codeField.getText().toString();
                if(code.length() == 6){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter 6 Digit OTP, and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void doSmsStuff(String phoneNumber){

//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(), "Verification failed! Please try again!", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(getApplicationContext(), "OTP sent!", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            doLinkingStuff(credential, user);



                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), "Invalid code!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void doLinkingStuff(PhoneAuthCredential credential, FirebaseUser user){

        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");

        AuthCredential cred = EmailAuthProvider.getCredential(username, password);

        user.linkWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "linkWithCredential:success");

                    if(Constants.category.equals("student")){
                        Log.d(TAG, "Student prof update");
                        Student student = (Student) getIntent().getSerializableExtra("user");

                        registerStudentOnDB(student, task);


                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("student").build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Display name: ", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                }
                            }
                        });

                    }
                    else if(Constants.category.equals("teacher")){
                        Log.d(TAG, "teacher prof update");
                        Teacher teacher = (Teacher) getIntent().getSerializableExtra("user");
                        mDatabase.child("teachers").child(user.getUid()).setValue(teacher);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("teacher").build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Display name: ", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                }
                            }
                        });

                    }


                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.getException());
                    Toast.makeText(PhoneNoActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void registerStudentOnDB(Student student, Task<AuthResult> task){
        FirebaseUser user = task.getResult().getUser();
        String uid = user.getUid();

        saveCandidatePhoto(uid, student);
    }


    private void saveCandidatePhoto(String key, Student student){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");

        Uri file = Uri.fromFile(new File(student.getImgDownloadUrl()));
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        String modifiedName = key+"."+extension;
        Log.d(TAG, "FIle Name : "+modifiedName);
        StorageReference profRef = imagesRef.child(modifiedName);

        UploadTask uploadTask = profRef.putFile(file);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnCompleteListener(PhoneNoActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Task successful");
                    task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(PhoneNoActivity.this, new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "OnComplete");
                                student.setImgDownloadUrl(task.getResult().toString());
                                mDatabase.child("students").child(key).setValue(student).addOnCompleteListener(PhoneNoActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "Database writing complete");
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "Success writing to databse");
                                            startActivity(new Intent(PhoneNoActivity.this, TeacherHome.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed");
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Registration");
        builder.setMessage("Are you sure you want to exit registration?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(PhoneNoActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
