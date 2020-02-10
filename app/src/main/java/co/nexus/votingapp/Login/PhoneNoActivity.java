package co.nexus.votingapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.concurrent.TimeUnit;

import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.Helpers.Teacher;
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
        sendOTPButton = findViewById(R.id.phoneSendOTP);

        verifyButton.setVisibility(View.INVISIBLE);
        numberField.setText(phone);

        codeField = findViewById(R.id.smsCodeField);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberField.getText().toString();
                if(number.length() == 10){
                    sendOTPButton.setVisibility(View.INVISIBLE);
                    codeField.setVisibility(View.VISIBLE);
                    verifyButton.setVisibility(View.VISIBLE);
                    doSmsStuff(countryCode+number);
                }
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                sendOTPButton.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.INVISIBLE);

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
                    FirebaseUser user = task.getResult().getUser();

                    if(category.equals("student")){
                        Student student = (Student) getIntent().getSerializableExtra("user");
                        mDatabase.child("students").child(user.getUid()).setValue(student);

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

                        startActivity(new Intent(PhoneNoActivity.this, StudentHome.class));
                    }
                    else if(category.equals("teacher")){
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

                        startActivity(new Intent(PhoneNoActivity.this, TeacherHome.class));
                    }


                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.getException());
                    Toast.makeText(PhoneNoActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });






//        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success");
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    linkCredentials(credential, user);
////                    updateUI(user);
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.getException());
////                    Toast.makeText(PhoneNoActivity.this, "Authentication failed.",
////                            Toast.LENGTH_SHORT).show();
////                    updateUI(null);
//                }
//            }
//        });
    }
//
//    private void linkCredentials(PhoneAuthCredential credential, FirebaseUser user){
//        user.linkWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "linkWithCredential:success");
//                    FirebaseUser user = task.getResult().getUser();
//
//                    startActivity(new Intent(PhoneNoActivity.this, StudentHome.class));
////                    updateUI(user);
//                } else {
//                    Log.w(TAG, "linkWithCredential:failure", task.getException());
////                    Toast.makeText(PhoneNoActivity.this, "Authentication failed.",
////                            Toast.LENGTH_SHORT).show();
////                    updateUI(null);
//                }
//            }
//        });
//    }



}
