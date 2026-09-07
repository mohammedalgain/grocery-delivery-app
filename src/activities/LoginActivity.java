package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    DBHelper dbHelper;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        dbHelper = new DBHelper(this);

        TextView signUpTextView = findViewById(R.id.tvSignUp);
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Validation for empty fields
            if (user.isEmpty()) {
                username.setError("Username is required");
                username.requestFocus();
                return;
            }

            if (pass.isEmpty()) {
                password.setError("Password is required");
                password.requestFocus();
                return;
            }

            // Authenticate the user
            if (dbHelper.checkUser(user, pass)) {
                // Fetch user role
                String role = dbHelper.getUserRole(user);

                // Log role for debugging purposes
                Log.d(TAG, "User Role: " + role);

                // Save user details in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", user);
                editor.putString("role", role);
                editor.apply();

                // Redirect based on role
                if ("Admin".equalsIgnoreCase(role)) {
                    Toast.makeText(LoginActivity.this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                } else if ("User".equalsIgnoreCase(role)) {
                    Toast.makeText(LoginActivity.this, "Welcome, " + user + "!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                }

                // Close LoginActivity after successful login
                finish();
            } else {
                // Invalid credentials
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Invalid login attempt for username: " + user);
            }
        });
    }
}
