package co.nexus.votingapp.Admin;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.nexus.votingapp.Helpers.Notification;
import co.nexus.votingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminNotificationFragment extends Fragment {
    private DatabaseReference mRef;
    private final String TAG = "AdminNotfFragment";
    private ProgressDialog progressDialog;


    public AdminNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_notification, container, false);

        EditText editTextAdminNotf = root.findViewById(R.id.editTextAdminNotification);
        EditText editTextAdminNotfTitle = root.findViewById(R.id.editTextAdminTitleNotf);
        Button buttonAdminSubmitNotf = root.findViewById(R.id.buttonSubmitAdminNotification);

        mRef = FirebaseDatabase.getInstance().getReference();

        buttonAdminSubmitNotf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit Button Clicked");
                String text = editTextAdminNotf.getText().toString();
                String title = editTextAdminNotfTitle.getText().toString();
                if(!TextUtils.isEmpty(text) && !TextUtils.isEmpty(title)){
                    progressDialog = showProgressDialog();

                    long time = Notification.getCurrentTime();
                    Notification notification = new Notification(title, text, time);

                    String key = mRef.child("notifications").push().getKey();
                    mRef.child("notifications").child(key).setValue(notification).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Notification submitted succesfully!" , Toast.LENGTH_SHORT).show();
                            editTextAdminNotf.setText("");
                            progressDialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Please fill out all the fields and try again!" , Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }


}
