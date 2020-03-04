package co.nexus.votingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import co.nexus.votingapp.Helpers.Candidate;
import co.nexus.votingapp.Helpers.Notification;
import co.nexus.votingapp.MainActivity;
import co.nexus.votingapp.R;

public class AdminResultActivity extends AppCompatActivity {
    private final String TAG = "AdminResultActivity";
    private DatabaseReference mRef;
    private String department = "none";
    private ArrayList<Candidate> year_one_data = new ArrayList<>();
    private ArrayList<Candidate> year_two_data = new ArrayList<>();
    private ArrayList<Candidate> year_three_data = new ArrayList<>();
    private ListView listViewFirstYear, listViewSecondYear, listViewThirdYear;
    private ProgressDialog progressDialog;
    private Button resPublishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_result);

        department = getIntent().getStringExtra("department");
        Log.d(TAG, department);

        progressDialog = showProgressDialog();


        listViewFirstYear = findViewById(R.id.listViewFirstYear);
        listViewSecondYear = findViewById(R.id.listViewSecondYear);
        listViewThirdYear = findViewById(R.id.listViewThirdYear);
        resPublishButton = findViewById(R.id.resPublishButton);

        mRef = FirebaseDatabase.getInstance().getReference();

        getData();

        resPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Publish button pressed");
                sendNotification();
            }
        });

    }

    private void sendNotification(){
        if(!year_one_data.isEmpty()){
            for (Candidate candidate : year_one_data) {
                if (candidate.getGender().equals("M")) {
                    String msg = candidate.getName() + " of " + candidate.getDepartment() + " year " + candidate.getYearOfStudy() + " won the election";
                    Notification.makeNotification(this, "Election Results", msg);
                    break;
                }
            }

            for (Candidate candidate : year_one_data) {
                if (candidate.getGender().equals("F")) {
                    String msg = candidate.getName() + " of " + candidate.getDepartment() + " year " + candidate.getYearOfStudy() + " won the election";
                    Notification.makeNotification(this, "Election Results", msg);
                    break;
                }
            }

        }

        if(!year_two_data.isEmpty()){
            for (Candidate candidate : year_two_data) {
                if (candidate.getGender().equals("M")) {
                    String msg = candidate.getName() + " of " + candidate.getDepartment() + " year " + candidate.getYearOfStudy() + " won the election";
                    Notification.makeNotification(this, "Election Results", msg);
                    break;
                }
            }

            for (Candidate candidate : year_two_data) {
                if (candidate.getGender().equals("F")) {
                    String msg = candidate.getName() + " of " + candidate.getDepartment() + " year " + candidate.getYearOfStudy() + " won the election";
                    Notification.makeNotification(this, "Election Results", msg);
                    break;
                }
            }
        }

        if(!year_three_data.isEmpty()){
            for (Candidate candidate : year_three_data) {
                if (candidate.getGender().equals("M")) {
                    String msg = candidate.getName() + " of " + candidate.getDepartment() + " year " + candidate.getYearOfStudy() + " won the election";
                    Notification.makeNotification(this, "Election Results", msg);
                    break;
                }
            }

            for (Candidate candidate : year_three_data) {
                if (candidate.getGender().equals("F")) {
                    String msg = candidate.getName() + " of " + candidate.getDepartment() + " year " + candidate.getYearOfStudy() + " won the election";
                    Notification.makeNotification(this, "Election Results", msg);
                    break;
                }
            }
        }
    }


    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }

    private void getData(){
        mRef.child("candidate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Data Obtained");
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Candidate candidate = snap.getValue(Candidate.class);
                    if(candidate.getDepartment().equals(department)){
                        switch (candidate.getYearOfStudy()){
                            case 1: {
                                year_one_data.add(candidate);
                                break;
                            }
                            case 2: {
                                year_two_data.add(candidate);
                                break;
                            }
                            case 3: {
                                year_three_data.add(candidate);
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }

                doAdpaterStuff();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Data onCancelled");
                progressDialog.dismiss();
            }
        });
    }



    private void doAdpaterStuff(){
        Collections.sort(year_one_data);
        Collections.sort(year_two_data);
        Collections.sort(year_three_data);
        Log.d(TAG,"1st year count : "+year_one_data.size());
        Log.d(TAG,"2nd year count : "+year_two_data.size());
        Log.d(TAG,"3rd year count : "+year_three_data.size());
        ResultAdapter adapter1 = new ResultAdapter(this, year_one_data);
        ResultAdapter adapter2 = new ResultAdapter(this, year_two_data);
        ResultAdapter adapter3 = new ResultAdapter(this, year_three_data);
        listViewFirstYear.setAdapter(adapter1);
        listViewSecondYear.setAdapter(adapter2);
        listViewThirdYear.setAdapter(adapter3);

        ListUtils.setDynamicHeight(listViewFirstYear);
        ListUtils.setDynamicHeight(listViewSecondYear);
        ListUtils.setDynamicHeight(listViewThirdYear);


        progressDialog.dismiss();
    }

    public void backButtonPressed(View v){
        onBackPressed();
    }


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
