package co.nexus.votingapp.Admin;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Teacher;
import co.nexus.votingapp.R;
import co.nexus.votingapp.Teacher.TeacherAdapter;


public class AdminInboxFragment extends Fragment {
    private final String TAG = "AdminInboxActivity";
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private RecyclerView recyclerView;
    private TeacherAdapter mAdapter;
    private ProgressDialog progressDialog;

    public AdminInboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_inbox, container, false);

        progressDialog = showProgressDialog();


        recyclerView = root.findViewById(R.id.recyclerViewAdminInbox);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TeacherAdapter(getContext(), teachers, keys);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        getData();


        return root;
    }


    private void getData(){
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("teachers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                teachers.clear();
                keys.clear();
                for(DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                    Teacher teacher = singleSnapShot.getValue(Teacher.class);
                    if(!teacher.isConfirmed() && !teacher.isRemoved()){
                        teachers.add(teacher);
                        keys.add(singleSnapShot.getKey());
                        Log.d(TAG, "Found teacher that is not confirmed");
                    }
                }

                Log.d(TAG, "Size : "+teachers.size());

                doAdapterStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
                progressDialog.dismiss();
            }
        });
    }

    private void doAdapterStuff(){
        mAdapter = new TeacherAdapter(getContext(), teachers, keys);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

}
