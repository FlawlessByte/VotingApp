package co.nexus.votingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import co.nexus.votingapp.Admin.AdminLoginActivity;
import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Login.SignInActivity;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();
        initNavigationMenu();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Drawer Simple Light");
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
                Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                actionBar.setTitle(item.getTitle());

                if(item.getTitle().equals("Student Login")){
                    Constants.category = "student";
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
                else if(item.getTitle().equals("Teacher Login")){
                    Constants.category = "teacher";
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
                else if(item.getTitle().equals("Admin Login")){
                    Log.d(TAG, "Admin login clicked");
                    startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                }




                drawer.closeDrawers();
                return true;
            }
        });

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }
}
