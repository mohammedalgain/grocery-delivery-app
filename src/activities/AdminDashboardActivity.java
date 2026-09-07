package com.example.grocerydeliveryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    Button btnManageUsers, btnManageProducts, btnViewOrders, btnLogout;
    TextView tvWelcome;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcomeAdmin);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageProducts = findViewById(R.id.btnManageProducts); // New button for Manage Products
        btnViewOrders = findViewById(R.id.btnViewOrders);
        btnLogout = findViewById(R.id.btnLogout);

        // Retrieve Admin username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String adminUsername = sharedPreferences.getString("username", "Admin");

        // Set welcome message
        tvWelcome.setText(String.format("Welcome, %s!", adminUsername));

        // Manage Users button
        btnManageUsers.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, ManageUsersActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "Error opening Manage Users screen", Toast.LENGTH_SHORT).show();
            }
        });

        // Manage Products button
        btnManageProducts.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, ManageProductsActivity.class)); // Ensure ManageProductsActivity exists
            } catch (Exception e) {
                Toast.makeText(this, "Error opening Manage Products screen", Toast.LENGTH_SHORT).show();
            }
        });

        // View Orders button
        btnViewOrders.setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, ManageOrdersActivity.class)); // Ensure ManageOrdersActivity exists
            } catch (Exception e) {
                Toast.makeText(this, "Error opening View Orders screen", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            // Confirmation for Logout
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Clear SharedPreferences and navigate to Login screen
                        sharedPreferences.edit().clear().apply();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
