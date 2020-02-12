package co.nexus.votingapp.Admin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import co.nexus.votingapp.Helpers.NewsFeed;
import co.nexus.votingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFeedsFragment extends Fragment {
    private DatabaseReference mRef;
    private final String TAG = "AdminFeedsFragment";
    public AdminFeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_feeds, container, false);

        EditText editTextAdminFeed = root.findViewById(R.id.editTextAdminFeed);
        Button buttonAdminSubmitFeed = root.findViewById(R.id.buttonSubmitAdminFeeds);

        mRef = FirebaseDatabase.getInstance().getReference();

        buttonAdminSubmitFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit Button Clicked");
                String text = editTextAdminFeed.getText().toString();
                if(!TextUtils.isEmpty(text)){

                    long time = NewsFeed.getCurrentTime();
                    NewsFeed newsFeed = new NewsFeed(text, time);

                    String key = mRef.child("newsfeeds").push().getKey();
                    mRef.child("newsfeeds").child(key).setValue(newsFeed).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "News feed submitted succesfully!" , Toast.LENGTH_SHORT).show();
                            editTextAdminFeed.setText("");
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "No text to submit!" , Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

}
