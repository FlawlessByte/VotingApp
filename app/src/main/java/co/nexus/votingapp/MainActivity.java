package co.nexus.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import co.nexus.votingapp.Admin.AdminLoginActivity;
import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Helpers.Notification;
import co.nexus.votingapp.Helpers.NotificationAdapter;
import co.nexus.votingapp.Login.SignInActivity;
import co.nexus.votingapp.Student.StudentHome;
import co.nexus.votingapp.Teacher.TeacherHome;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private final String TAG = "MainActivity";
    private DatabaseReference mRef;
    private RecyclerView recyclerViewNotf, recyclerViewNews;
    private NotificationAdapter notfAdapter, newsAdapter;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private ArrayList<Notification> newsfeeds = new ArrayList<>();
    private int taskCount = 2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Manage the signed in users
        SharedPreferences sharedPref = getSharedPreferences(Constants.user_prof,Context.MODE_PRIVATE);
        String user = sharedPref.getString("currentUser", "none");
        Log.d(TAG, "Current User : "+user);

        if(FirebaseAuth.getInstance().getUid() != null)
            switch (user){
                case "student": {
                    startActivity(new Intent(MainActivity.this, StudentHome.class));
                    finish();
                    break;
                }
                case "teacher": {
                    startActivity(new Intent(MainActivity.this, TeacherHome.class));
                    finish();
                    break;
                }
            }
        else
            Log.d(TAG, "Auth null");



        progressDialog = showProgressDialog();

        mRef = FirebaseDatabase.getInstance().getReference();
        recyclerViewNews = findViewById(R.id.recyclerViewNewsFeeds);
        recyclerViewNotf = findViewById(R.id.recyclerViewNotification);
        recyclerViewNews.setHasFixedSize(true);
        recyclerViewNotf.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplication());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplication());
        recyclerViewNotf.setLayoutManager(layoutManager1);
        recyclerViewNews.setLayoutManager(layoutManager2);
        notfAdapter = new NotificationAdapter(this, notifications);
        newsAdapter = new NotificationAdapter(this, newsfeeds);
        recyclerViewNews.setAdapter(newsAdapter);
        recyclerViewNotf.setAdapter(notfAdapter);
        notfAdapter.notifyDataSetChanged();
        newsAdapter.notifyDataSetChanged();

        getData();

        initToolbar();
        initNavigationMenu();
    }

    private void getData(){
        getNewsFeeds();
        getNotifications();
    }

    private void dismissProgressDialog(){
        taskCount--;
        if(taskCount == 0){
            progressDialog.dismiss();
        }
    }

    private void getNewsFeeds(){
        mRef.child("newsfeeds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsfeeds.clear();
                Log.d(TAG, "News feeds obtained");
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Notification feed = snap.getValue(Notification.class);
                    newsfeeds.add(feed);
                }
                newsAdapter = new NotificationAdapter(MainActivity.this, newsfeeds);
                recyclerViewNews.setAdapter(newsAdapter);
                newsAdapter.notifyDataSetChanged();
                dismissProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "News feeds : onCancelled");
                dismissProgressDialog();
            }
        });
    }

    private void getNotifications(){
        mRef.child("notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifications.clear();
                Log.d(TAG, "Notifications obtained");
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Notification notf = snap.getValue(Notification.class);
                    notifications.add(notf);
                }
                Collections.sort(notifications);
                notfAdapter = new NotificationAdapter(MainActivity.this, notifications);
                recyclerViewNotf.setAdapter(notfAdapter);
                notfAdapter.notifyDataSetChanged();
                dismissProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "News feeds : onCancelled");
                dismissProgressDialog();
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Voting App");
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
//                Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
//                actionBar.setTitle(item.getTitle());

                switch (item.getTitle().toString()){
                    case "Student Login": {
                        Constants.category = "student";
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        break;
                    }
                    case "Teacher Login": {
                        Constants.category = "teacher";
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        break;
                    }
                    case "Admin Login": {
                        Log.d(TAG, "Admin login clicked");
                        startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                        finish();
                        break;
                    }
                    case "Gallery": {
                        Log.d(TAG, "Gallery clicked");
                        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                        finish();
                        break;
                    }
                    case "About": {
                        Log.d(TAG, "About clicked");
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        finish();
                        break;
                    }

                }


                drawer.closeDrawers();
                return true;
            }
        });

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }

    private ProgressDialog showProgressDialog(){
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please wait!");
        dialog.show();
        return dialog;
    }
}
