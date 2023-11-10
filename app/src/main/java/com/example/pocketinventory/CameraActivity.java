package com.example.pocketinventory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * This class is the camera activity of the app. It gives the user access to the camera to take
 * photos of their item(s) that are to be stored in the list.
 */
public class CameraActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);




        // For the navigation panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_camera);

        // This controls the navigation between the different activities
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(CameraActivity.this, HomePageActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_camera) {
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                startActivity(new Intent(CameraActivity.this, UserProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
