package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText etUsername, etPassword;
    private RadioGroup roleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbHelper = new DBHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        roleGroup = findViewById(R.id.roleGroup);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = roleGroup.getCheckedRadioButtonId() == R.id.radioAdmin ? "Admin" : "User";

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addUser(username, password, role)) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add user. Username might already exist.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
