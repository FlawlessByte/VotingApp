package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Helpers.Teacher;
import co.nexus.votingapp.MainActivity;
import co.nexus.votingapp.R;

public class TeacherHome extends AppCompatActivity {
    private Button buttonAddCandidate, buttonTeacherInbox, buttonTeacherLogOut;
    private final String TAG = "TeacherHome";
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private boolean isTeacherAuthorized;
    private String uid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        progressDialog = showProgressDialog();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        Log.d(TAG, "UID : "+uid);
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("teachers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                Teacher teacher = dataSnapshot.getValue(Teacher.class);
                isTeacherAuthorized = teacher.isConfirmed();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
                progressDialog.dismiss();
            }
        });



        buttonAddCandidate = findViewById(R.id.buttonAddCandidate);
        buttonTeacherInbox = findViewById(R.id.buttonTeacherInbox);
        buttonTeacherLogOut = findViewById(R.id.buttonTeacherSignOut);

        mAuth = FirebaseAuth.getInstance();

        buttonAddCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Candidate clicked!");
                if(isTeacherAuthorized)
                    startActivity(new Intent(TeacherHome.this, AddCandidateActivity.class));
                else
                    Toast.makeText(TeacherHome.this, "Your account is not yet confirmed by the teacher!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonTeacherInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Inbox clicked!");
                if(isTeacherAuthorized)
                    startActivity(new Intent(TeacherHome.this, TeacherInboxActivity.class));
                else
                    Toast.makeText(TeacherHome.this, "Your account is not yet confirmed by the teacher!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonTeacherLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Sign Out clicked!");
                mAuth.signOut();
                startActivity(new Intent(TeacherHome.this, MainActivity.class));
            }
        });

    }


    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "OnBackPressed");
        SharedPreferences pref = getSharedPreferences(Constants.user_prof, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("currentUser", "teacher");
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
}
