package co.nexus.votingapp.Admin;


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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.nexus.votingapp.Helpers.Timer;
import co.nexus.votingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminTimerFragment extends Fragment {
    private final String TAG = "AdminTimerFragment";


    public AdminTimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_timer, container, false);

        Button setButton = root.findViewById(R.id.setTimerButtonAdmin);
        EditText mmEditText = root.findViewById(R.id.mmEditTextAdmin);
        EditText ssEditText = root.findViewById(R.id.ssEditTextAdmin);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Timer set button pressed");

                if(TextUtils.isEmpty(mmEditText.getText().toString()) || TextUtils.isEmpty(ssEditText.getText().toString())){
                    //Empty fields
                    Log.d(TAG, "Empty fields");
                    Toast.makeText(getContext(), "Please make sure that the fields are not emply!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int minutes = Integer.parseInt(mmEditText.getText().toString());
                    int seconds = Integer.parseInt(ssEditText.getText().toString());
                    updateTimerValuesInDB(minutes,seconds);
                }
            }
        });


        return root;
    }


    private void updateTimerValuesInDB(int minutes, int seconds){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Timer timer = new Timer(minutes, seconds);
        ref.child("timer").setValue(timer).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Timer set successfully!" , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Timer updation failed!" , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
