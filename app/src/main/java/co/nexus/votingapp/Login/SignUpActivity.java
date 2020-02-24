package co.nexus.votingapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.MainActivity;
import co.nexus.votingapp.R;
import co.nexus.votingapp.Student.StudentRegister;
import co.nexus.votingapp.Teacher.TeacherRegister;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText emailInputSignUp, passwordInputSignUp, confirmPasswordInputSignUp;
    private Button buttonSignUp;
    private String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailInputSignUp = findViewById(R.id.emailInputSignUp);
        passwordInputSignUp = findViewById(R.id.passwordInputSignUp);
        confirmPasswordInputSignUp = findViewById(R.id.confirmPasswordInputSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "SignUpButton Clicked");

                doSignUpStuff(emailInputSignUp.getText().toString(), passwordInputSignUp.getText().toString(), confirmPasswordInputSignUp.getText().toString());
//                createUser(emailInputSignUp.getText().toString(), passwordInputSignUp.getText().toString());
            }
        });
    }


    private void doSignUpStuff(String username, String password, String passwordConfirm){
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)){
            Toast.makeText(this, "Please fill out the fields and try again!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            Toast.makeText(this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(passwordConfirm)){
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(this, "The password length must be atleast 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Constants.category.equals("student")){
            Log.d(TAG, "Student sign in");
            Intent intent = new Intent(SignUpActivity.this, StudentRegister.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        }
        else {
            Log.d(TAG, "teacher sign in");
            Intent intent = new Intent(SignUpActivity.this, TeacherRegister.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        }

    }

    private void createUser(String username, String password){
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    if(Constants.category.equals("student")){
                        Intent intent = new Intent(SignUpActivity.this, StudentRegister.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(SignUpActivity.this, TeacherRegister.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
