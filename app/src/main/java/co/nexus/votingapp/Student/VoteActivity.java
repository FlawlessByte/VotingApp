package co.nexus.votingapp.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.Helpers.SpacingItemDecoration;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.Helpers.Timer;
import co.nexus.votingapp.R;

public class VoteActivity extends AppCompatActivity {
    private final String TAG = "VoteActivity";
    private TextView textViewTimerValue;
    private RecyclerView recyclerView;
    private ArrayList<Candidate> candidates;
    private ArrayList<String> keys;
    private int size;
    private String uid;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private int taskCount = 1;
    private VoteAdapter mAdapter;
    private Student student;
    private Timer timer;
    private String gender;
    private ProgressDialog progressDialog;
    private Button notaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        gender = getIntent().getStringExtra("gender");
        candidates = new ArrayList<>();
        keys = new ArrayList<>();

        textViewTimerValue = findViewById(R.id.textViewTimerValue);
        recyclerView = findViewById(R.id.recyclerViewVoteCandidate);
        notaButton = findViewById(R.id.notaButton);


        notaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Nota button clicked!");
                showConfirmDialog();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, dpToPx(2), true));

        mAdapter = new VoteAdapter(this, candidates, keys,  gender);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mRef = FirebaseDatabase.getInstance().getReference();

        getTimerValue();

        getStudentData(uid);

    }

    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

    private void getTimerValue(){

        progressDialog = showProgressDialog();

        mRef.child("timer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "TImer data obtained");
                timer = dataSnapshot.getValue(Timer.class);
//                doTimerStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Time data cancelled");
            }
        });
    }

    private void doTimerStuff(){
        int millsInFuture = timer.minutes * 100000;
        CountDownTimer cdt = new CountDownTimer(millsInFuture, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimerValue.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));;
            }

            public void onFinish() {
//                mTextField.setText("done!");
                Log.d(TAG , "Timer finished!");
                showTimerFinishedAlert();
            }
        }.start();


    }



    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vote NOTA");
        builder.setMessage("Are you sure you want to vote NOTA?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "Dialog positive button clicked");
                updateSharedPreference();
                onBackPressed();
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
    }

    private void updateSharedPreference(){
        String val = "";
        switch (gender){
            case "M": {
                val = "vote_male_candidate";
                break;
            }
            case "F": {
                val = "vote_female_candidate";
                break;
            }
            default:
                break;
        }

        String uid = FirebaseAuth.getInstance().getUid();
        SharedPreferences sharedPref = getSharedPreferences(uid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(val, false);
        editor.apply();
    }


    private void showTimerFinishedAlert(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning_timeout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((Button) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), ((Button) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Warning ok button pressed");
                dialog.dismiss();
                updateSharedPreference();
                onBackPressed();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void doAdapterStuff(){
        taskCount--;
        if(taskCount==0) {
            Log.d(TAG, "Task count 0");
            mAdapter = new VoteAdapter(this, candidates, keys, gender);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
            doTimerStuff();
        }
    }


    private void getStudentData(String uid){
        mRef.child("students").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Student data obtained");
                student = dataSnapshot.getValue(Student.class);
                getCandidates();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Students cancelled");
            }
        });
    }

    private void getCandidates(){
        mRef.child("candidate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Candidate data obtained");
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Candidate candidate = snap.getValue(Candidate.class);

                    if(candidate.getDepartment().equals(student.getDepartment()) && candidate.getYearOfStudy() == student.getYearOfStudy()
                                && candidate.getGender().equals(gender)){
                        //Candidate match
                        Log.d(TAG, "candidate match");
                        candidates.add(candidate);
                        keys.add(snap.getKey());
                    }
                }
                doAdapterStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Candidates cancelled");
            }
        });
    }


    public void backButtonPressed(View view){
        Log.d(TAG, "Back button pressed");
        onBackPressed();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
