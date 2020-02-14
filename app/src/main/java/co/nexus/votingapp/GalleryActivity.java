package co.nexus.votingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import co.nexus.votingapp.Admin.AdminGalleryFragment;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frameLayoutContainer, new AdminGalleryFragment(null));
        ft.commit();

    }

    public void backButtonPressed(View v){
        onBackPressed();
    }
}
