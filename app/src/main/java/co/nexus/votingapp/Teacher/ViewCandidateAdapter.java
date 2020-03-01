package co.nexus.votingapp.Teacher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;
import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.R;

public class ViewCandidateAdapter extends RecyclerView.Adapter<ViewCandidateAdapter.ViewHolder> {
    private final String TAG = "ViewCandidateAdapter";
    private ArrayList<Candidate> candidates;
    private ArrayList<String> keys;
    private Context context;

    public ViewCandidateAdapter(Context context, ArrayList<Candidate> candidates, ArrayList<String> keys) {
        this.candidates = candidates;
        this.keys = keys;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewCandidateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_view_candidate, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCandidateAdapter.ViewHolder holder, int position) {
        Candidate candidate = candidates.get(position);

        holder.nameTextView.setText(candidate.getName());
        holder.descTextView.setText(candidate.getDescription());
        holder.deptTextView.setText(candidate.getDepartment());
        holder.yosTextView.setText(""+candidate.getYearOfStudy());


        Glide.with(context)
                .load(candidate.getImgPath()).into(holder.profImageView);

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Accept button clicked!");
                acceptCandidate(position);
            }
        });


        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Remove Button clicked");
                showConfirmDialog(position);
            }
        });
    }


    private void acceptCandidate(int pos){
        Log.d(TAG, "Accept candidate");

        ProgressDialog dialog = showProgressDialog();
        dialog.show();

        String key = keys.get(pos);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("candidate").child(key).child("reviewed").setValue(true);
        mRef.child("candidate").child(key).child("confirmed").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "reviewed updated");
                dialog.dismiss();
            }
        });
    }


    private void showConfirmDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove Candidate");
        builder.setMessage("Are you sure you want to remove this candidate?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "Dialog positive button clicked");
                rejectCandidate(pos);
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
    }

    private void rejectCandidate(int pos){
        Log.d(TAG, "Remove candidate");

        ProgressDialog dialog = showProgressDialog();
        dialog.show();

        String key = keys.get(pos);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("candidate").child(key).child("reviewed").setValue(true);
        mRef.child("candidate").child(key).child("confirmed").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "reviewed updated");
                dialog.dismiss();
            }
        });

//        mRef.child("candidate").child(key).removeValue().addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Log.d(TAG, "onComplete");
//                if(task.isSuccessful()){
//                    Log.d(TAG, "Successfull removing candidate");
//                    Toast.makeText(context, "Successfully removed candidate!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//                else{
//                    Toast.makeText(context, "Error removing candidate!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//            }
//        });


    }


    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait!");
        return dialog;
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircularImageView profImageView;
        public TextView nameTextView, descTextView, yosTextView, deptTextView;
        public Button removeButton, acceptButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profImageView = itemView.findViewById(R.id.item_view_candidate_imageview);
            nameTextView = itemView.findViewById(R.id.item_view_candidate_name_textview);
            removeButton = itemView.findViewById(R.id.item_remove_candidate_button);
            descTextView = itemView.findViewById(R.id.item_view_candidate_desc_textview);
            yosTextView = itemView.findViewById(R.id.item_view_candidate_yos_textview);
            deptTextView = itemView.findViewById(R.id.item_view_candidate_dept_textview);
            acceptButton = itemView.findViewById(R.id.item_accept_candidate_button);

        }
    }
}