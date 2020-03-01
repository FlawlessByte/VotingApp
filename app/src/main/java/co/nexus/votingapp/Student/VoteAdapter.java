package co.nexus.votingapp.Student;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.Helpers.GlideApp;
import co.nexus.votingapp.R;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    private final String TAG = "VoteAdapter";
    private ArrayList<Candidate> candidates;
    private ArrayList<String> keys;
    private Context context;
    private String gender;

    public VoteAdapter(Context context, ArrayList<Candidate> candidates, ArrayList<String> keys,  String gender) {
        this.candidates = candidates;
        this.keys = keys;
        this.gender = gender;
        this.context = context;
    }

    @NonNull
    @Override
    public VoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_vote_candidate, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VoteAdapter.ViewHolder holder, int position) {
        Candidate candidate = candidates.get(position);

        holder.nameTextView.setText(candidate.getName());
        holder.descTextView.setText(candidate.getDescription());


        Glide.with(context)
            .load(candidate.getImgPath()).into(holder.profImageView);

        holder.voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Vote Button clicked");
                showConfirmDialog(position);
            }
        });
    }


    private void showConfirmDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Vote Candidate");
        builder.setMessage("Are you sure you want to vote this candidate?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "Dialog positive button clicked");
                updateVoteCount(pos);
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
    }

    private void updateVoteCount(int pos){
        Log.d(TAG, "update vote count");

        ProgressDialog dialog = showProgressDialog();

        String key = keys.get(pos);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("candidate").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Candidate candidate = dataSnapshot.getValue(Candidate.class);
                candidate.setVoteCount(candidate.getVoteCount()+1);
                mRef.child("candidate").child(key).setValue(candidate).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Vote count updated!");
                        dialog.dismiss();
                        updateSharedPreference();
                        Toast.makeText(context, "Voted succesfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener((Activity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Vote count update failed!");
                        dialog.dismiss();
                        Toast.makeText(context, "Sorry, some error occured!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to get candidate. onCancelled");
            }
        });
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
        SharedPreferences sharedPref = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(val, false);
        editor.apply();

        ((Activity) context).onBackPressed();
    }

    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircularImageView profImageView;
        public TextView nameTextView, descTextView;
        public Button voteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profImageView = itemView.findViewById(R.id.item_vote_prof_imageview);
            nameTextView = itemView.findViewById(R.id.item_vote_name_textview);
            voteButton = itemView.findViewById(R.id.item_vote_button);
            descTextView = itemView.findViewById(R.id.item_vote_desc_textview);

        }
    }
}
