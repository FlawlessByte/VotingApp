package co.nexus.votingapp.Admin;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminResultFragment extends Fragment {
    private final String TAG = "AdminResultFeagment";


    public AdminResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_admin_result, container, false);

        ArrayList<String> depts = new ArrayList<>();
        for(int i = 0; i < Constants.departments.length; i++)
            depts.add(Constants.departments[i]);

        DepartmentAdapter adapter = new DepartmentAdapter(getContext(), depts);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewResultDepartment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return root;
    }

}
