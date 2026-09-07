package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Set the main activity layout

        // Check if the user is logged in
        if (isLoggedIn()) {
            // Redirect to the appropriate dashboard (Client or Agent)
            startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
        } else {
            // If not logged in, direct to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish();  // To avoid back stack when returning from login
    }

    private boolean isLoggedIn() {
        // Check if user session exists, for example:
        return getSharedPreferences("user_session", MODE_PRIVATE)
                .getBoolean("is_logged_in", false);  // Return true if user is logged in
    }
}
