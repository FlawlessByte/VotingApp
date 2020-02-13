package co.nexus.votingapp.Helpers;

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

import co.nexus.votingapp.R;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private final String TAG = "StudentAdapter";
    private ArrayList<Student> students;
    private ArrayList<String> keys;
    private Context context;

    public StudentAdapter(Context context, ArrayList<Student> students, ArrayList<String> keys){
        this.students = students;
        this.context = context;
        this.keys = keys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.item_student_request, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);

        holder.studentNameTextView.setText(student.name);
        holder.studentEmailTextView.setText(student.email);
        holder.studentPhoneTextView.setText(student.phone);
        holder.studentAdmnNoTextView.setText(student.admissionNo);
        holder.studentDepartmentTextView.setText(student.department);
        holder.studentYOSTextView.setText(String.valueOf(student.yearOfStudy));
        holder.studentYOJTextView.setText(String.valueOf(student.yearOfJoining));

        holder.studentAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Student Accept Button clicked");
                Toast.makeText(context, "Please wait until the updation finishes!", Toast.LENGTH_SHORT).show();
                updateAsAccepted(position);

            }
        });

        holder.studentRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Student remove button clicked");
                Toast.makeText(context, "Please wait until the updation finishes!", Toast.LENGTH_SHORT).show();
                updateAsRemoved(position);
            }
        });

    }

    public void updateAsAccepted(int pos){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("students").child(keys.get(pos)).child("confirmed").setValue(true)
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Student status: Accepted!", Toast.LENGTH_SHORT).show();
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
        mRef.child("students").child(keys.get(pos)).child("removed").setValue(true)
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Student status: Removed!", Toast.LENGTH_SHORT).show();
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
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView studentNameTextView, studentEmailTextView, studentPhoneTextView, studentAdmnNoTextView,
                studentDepartmentTextView, studentYOSTextView, studentYOJTextView;
        public Button studentAcceptButton, studentRemoveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentNameTextView = itemView.findViewById(R.id.item_student_name_textview);
            studentEmailTextView = itemView.findViewById(R.id.item_student_email_textview);
            studentPhoneTextView = itemView.findViewById(R.id.item_student_phone_textview);
            studentAdmnNoTextView = itemView.findViewById(R.id.item_student_admn_no_textview);
            studentDepartmentTextView = itemView.findViewById(R.id.item_student_department_textview);
            studentYOSTextView = itemView.findViewById(R.id.item_student_yos_textview);
            studentYOJTextView = itemView.findViewById(R.id.item_student_yoj_textview);
            studentAcceptButton = itemView.findViewById(R.id.item_student_accept_button);
            studentRemoveButton = itemView.findViewById(R.id.item_student_remove_button);
        }
    }

    public void remove(int position) {
        students.remove(position);
        notifyItemRemoved(position);
    }
}
