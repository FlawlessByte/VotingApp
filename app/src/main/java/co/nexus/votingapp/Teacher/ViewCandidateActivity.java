package co.nexus.votingapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.R;

public class ViewCandidateActivity extends AppCompatActivity {
    private final String TAG = "ViewCandidateActivity";
    private RecyclerView recyclerView;
    private DatabaseReference mRef;
    private ArrayList<Candidate> candidates = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ViewCandidateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_candidate);

        recyclerView = findViewById(R.id.recyclerview_view_candidates);
        mRef = FirebaseDatabase.getInstance().getReference();


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ViewCandidateAdapter(this, candidates, keys);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        getData();
    }

    public void backButtonPressed(View view) {
        Log.d(TAG, "backButtonPressed");
        onBackPressed();
    }

    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

    private void getData(){
        mRef.child("candidate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                candidates.clear();
                keys.clear();
                for(DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                    Candidate candidate = singleSnapShot.getValue(Candidate.class);
                    candidates.add(candidate);
                    keys.add(singleSnapShot.getKey());
                }

                Log.d(TAG, "Size : "+candidates.size());

                doAdapterStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });
    }

    private void doAdapterStuff(){
        mAdapter = new ViewCandidateAdapter(this, candidates, keys);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

}
