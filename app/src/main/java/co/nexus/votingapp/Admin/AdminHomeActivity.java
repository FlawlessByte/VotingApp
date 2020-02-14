package co.nexus.votingapp.Admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import co.nexus.votingapp.R;

public class AdminHomeActivity extends AppCompatActivity {
    private final String TAG = "AdminHomeActivity";
    private DrawerLayout drawer;
    private ImageButton adminMenuButton, adminAddButton;
    private TextView adminMenuTitle;
//    private ActionBar actionBar;
//    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        adminMenuButton = findViewById(R.id.adminButtonMenu);
        adminAddButton = findViewById(R.id.adminAddButton);
        adminMenuTitle = findViewById(R.id.adminMenuTitleTextView);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.add(R.id.admin_fragment_placeholder, new AdminInboxFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();



        adminMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Menu Button clicked!");
                drawer.openDrawer(Gravity.LEFT);
            }
        });
    }

//    private void initToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
//        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setTitle("Drawer White");
////        Tools.setSystemBarColor(this, R.color.grey_10);
////        Tools.setSystemBarLight(this);
//    }

    public void onAdminMenuClicked(View v){
        String tag = v.getTag().toString();
        switch (tag){
            case "inbox":{
                Log.d(TAG, "Inbox clicked");
                adminMenuTitle.setText("Inbox");
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.admin_fragment_placeholder, new AdminInboxFragment());
                ft.commit();

                break;
            }
            case "result":{
                Log.d(TAG, "Result clicked");
                adminMenuTitle.setText("Result");
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.admin_fragment_placeholder, new AdminResultFragment());
                ft.commit();

                break;
            }
            case "gallery":{
                Log.d(TAG, "Gallery clicked");
                adminMenuTitle.setText("Gallery");
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.admin_fragment_placeholder, new AdminGalleryFragment(adminAddButton));
                ft.commit();

                break;
            }
            case "feeds":{
                Log.d(TAG, "Inbox clicked");
                adminMenuTitle.setText("News Feeds");
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.admin_fragment_placeholder, new AdminFeedsFragment());
                ft.commit();

                break;
            }
            case "notification":{
                Log.d(TAG, "Notification clicked");
                adminMenuTitle.setText("Notifications");
                // Begin the translogoutaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.admin_fragment_placeholder, new AdminNotificationFragment());
                ft.commit();

                break;
            }
            case "timer":{
                Log.d(TAG, "TImer clicked");
                adminMenuTitle.setText("Timer");
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.admin_fragment_placeholder, new AdminTimerFragment());
                ft.commit();

                break;
            }

            case "logout":{
                Log.d(TAG, "Logout clicked");
                onBackPressed();
                break;
            }

            default:{
                Log.d(TAG, "Default case");
                break;
            }
        }

        if(tag.equals("gallery"))
            adminAddButton.setVisibility(View.VISIBLE);
        else
            adminAddButton.setVisibility(View.INVISIBLE);

        drawer.closeDrawers();
    }
}
