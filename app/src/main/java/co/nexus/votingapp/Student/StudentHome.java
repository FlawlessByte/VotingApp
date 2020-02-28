package co.nexus.votingapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.MainActivity;
import co.nexus.votingapp.R;

public class StudentHome extends AppCompatActivity {
    private Button buttonFeePayment, buttonVoteCandidate, buttonSignOut;
    private MaterialRippleLayout layoutFeePayment, layoutVoteCandidate, layoutSignOut;
    private final String TAG = "StudentHome";
    private boolean isStudentAuthorized = false;
    private ProgressDialog progressDialog;
    private String uid;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        Log.d(TAG, "UID : "+uid);


        initialiseSharedPrefs();

        progressDialog = showProgressDialog();

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


        layoutFeePayment = findViewById(R.id.layoutFeePayment);
        layoutVoteCandidate = findViewById(R.id.layoutVoteCandidate);
        layoutSignOut = findViewById(R.id.layoutSignOut);


        layoutSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutConfirmDialog();
            }
        });

        layoutVoteCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudentAuthorized)
                    startActivity(new Intent(StudentHome.this, StudentVoteActivity.class));
                else
                    Toast.makeText(StudentHome.this, "Your account is not yet confirmed by the teacher!", Toast.LENGTH_SHORT).show();
            }
        });

        layoutFeePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudentAuthorized)
                    startActivity(new Intent(StudentHome.this, StudentPaymentActivity.class));
                else
                    Toast.makeText(StudentHome.this, "Your account is not yet confirmed by the teacher!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogOutConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                SharedPreferences pref = getSharedPreferences(Constants.user_prof, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("currentUser", "none");
                editor.apply();
                startActivity(new Intent(StudentHome.this, MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
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

    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed");
        SharedPreferences pref = getSharedPreferences(Constants.user_prof, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("currentUser", "student");
        editor.apply();
        showConfirmDialog();
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("Are you sure you want to exit the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    public void backButtonPressed(View view) {
        onBackPressed();
    }
}
