package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.RadioGroup;


public class SignUpActivity extends AppCompatActivity {

    EditText username, password, confirmPassword;
    RadioGroup roleGroup;
    Button signUpBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.etConfirmPassword);
        roleGroup = findViewById(R.id.roleGroup);
        signUpBtn = findViewById(R.id.btnSignUp);
        dbHelper = new DBHelper(this);

        // Set click listener for the Sign Up button
        signUpBtn.setOnClickListener(v -> {
            // Get input values
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            // Validate input fields
            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirmPass)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUserExists(user)) {
                Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected role
            String role;
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();
            if (selectedRoleId == R.id.radioUser) {
                role = "User";
            } else if (selectedRoleId == R.id.radioAdmin) {
                role = "Admin";
            } else {
                Toast.makeText(SignUpActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("SignUpActivity", "Selected Role: " + role);

            // Add the user to the database
            if (dbHelper.addUser(user, pass, role)) {
                Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                finish(); // Close SignUpActivity and go back to LoginActivity
            } else {
                Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


