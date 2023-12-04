package com.example.pocketinventory.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pocketinventory.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity {

    TextView userName;
    Button logout;
    private FirebaseAuth auth;
    private static final String PREFS_NAME = "MyPrefs";
    /**
     * Called when the User profile activity is starting or being reinitialized
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);
        auth = FirebaseAuth.getInstance();

        // This method allows the user to sign out of the app
        logout.setOnClickListener(view -> {
            // Sign the user out
            auth.signOut();

            // Remove the logged-in state from SharedPreferences
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            preferences.edit().remove("loggedIn").apply();

            // Provide feedback to the user
            Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();

            // Finish the UserProfileActivity and navigate back to the LoginActivity
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });

        // For the navigation panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        // This controls the navigation between the different activities
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                //startActivity(new Intent(UserProfileActivity.this, HomePageActivity.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                return true;
            }
            return false;
        });
    }

}
