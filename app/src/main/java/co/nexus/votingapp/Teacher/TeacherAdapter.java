package co.nexus.votingapp.Teacher;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Teacher;
import co.nexus.votingapp.R;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {
    private final String TAG = "TeacherAdapter";
    private ArrayList<Teacher> teachers;
    private ArrayList<String> keys;
    private Context context;

    public TeacherAdapter(Context context, ArrayList<Teacher> teachers, ArrayList<String> keys){
        this.teachers = teachers;
        this.context = context;
        this.keys = keys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.item_teacher_request, parent, false);
        // set the view's size, margins, paddings and layout parameters
        TeacherAdapter.ViewHolder vh = new TeacherAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {
        Teacher teacher = teachers.get(position);

        holder.teacherNameTextView.setText(teacher.getName());
        holder.teacherEmailTextView.setText(teacher.getEmail());
        holder.teacherPhoneTextView.setText(teacher.getPhone());
        holder.teacherTIDTextView.setText(teacher.getTid());
        holder.teacherDepartmentTextView.setText(teacher.getDepartment());
        holder.teacherYOJTextView.setText(String.valueOf(teacher.getYearOfJoining()));

        holder.teacherAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "teacher Accept Button clicked");
                Toast.makeText(context, "Please wait until the updation finishes!", Toast.LENGTH_SHORT).show();
                updateAsAccepted(position);

            }
        });

        holder.teacherRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "teacher remove button clicked");
                Toast.makeText(context, "Please wait until the updation finishes!", Toast.LENGTH_SHORT).show();
                updateAsRemoved(position);
            }
        });

    }

    public void updateAsAccepted(int pos){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("teachers").child(keys.get(pos)).child("confirmed").setValue(true)
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "teacher status: Accepted!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to accept!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateAsRemoved(int pos){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("teachers").child(keys.get(pos)).child("removed").setValue(true)
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "teacher status: Removed!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to remove!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView teacherNameTextView, teacherEmailTextView, teacherPhoneTextView, teacherTIDTextView,
                teacherDepartmentTextView, teacherYOJTextView;
        public Button teacherAcceptButton, teacherRemoveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            teacherNameTextView = itemView.findViewById(R.id.item_teacher_name_textview);
            teacherEmailTextView = itemView.findViewById(R.id.item_teacher_email_textview);
            teacherPhoneTextView = itemView.findViewById(R.id.item_teacher_phone_textview);
            teacherTIDTextView = itemView.findViewById(R.id.item_teacher_id_textview);
            teacherDepartmentTextView = itemView.findViewById(R.id.item_teacher_department_textview);
            teacherYOJTextView = itemView.findViewById(R.id.item_teacher_yoj_textview);
            teacherAcceptButton = itemView.findViewById(R.id.item_teacher_accept_button);
            teacherRemoveButton = itemView.findViewById(R.id.item_teacher_remove_button);
        }
    }

    public void remove(int position) {
        teachers.remove(position);
        notifyItemRemoved(position);
    }
}
