package com.example.grocerydeliveryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserDashboardActivity extends AppCompatActivity {

    private Button viewOrdersBtn, viewProfileBtn, logoutBtn, viewProductsBtn, viewCartBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Initialize buttons
        initializeViews();

        // Retrieve user data
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");
        String role = sharedPreferences.getString("role", "Client");

        // Display welcome message
        displayWelcomeMessage(username, role);

        // Set button listeners
        setupButtonListeners(sharedPreferences);
    }

    /**
     * Initializes the views.
     */
    private void initializeViews() {
        viewProductsBtn = findViewById(R.id.btnViewProducts); // Ensure the ID exists in XML
        viewOrdersBtn = findViewById(R.id.btnViewOrders);
        viewProfileBtn = findViewById(R.id.btnViewProfile);
        logoutBtn = findViewById(R.id.btnLogout);
        viewCartBtn = findViewById(R.id.btnViewCart); // Ensure this button is added in your XML
    }

    /**
     * Displays a welcome message for the user.
     */
    private void displayWelcomeMessage(String username, String role) {
        Toast.makeText(this, "Welcome, " + username + " (" + role + ")", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the button click listeners.
     */
    private void setupButtonListeners(SharedPreferences sharedPreferences) {
        viewProductsBtn.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardActivity.this, ProductListActivity.class));
        });


        viewCartBtn.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardActivity.this, CartActivity.class));
        });

        logoutBtn.setOnClickListener(v -> {
            logoutUser(sharedPreferences);
        });
    }

    /**
     * Logs out the user by clearing SharedPreferences and navigating to the login screen.
     */
    private void logoutUser(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(UserDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
