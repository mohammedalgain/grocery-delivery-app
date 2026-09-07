package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageUsersActivity extends AppCompatActivity implements UserActionListener {

    private DBHelper dbHelper;
    private RecyclerView rvUsers;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        dbHelper = new DBHelper(this);
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        // Fetch users and set up the adapter
        ArrayList<User> users = dbHelper.getAllUsersList();
        userAdapter = new UserAdapter(users, this, this);
        rvUsers.setAdapter(userAdapter);
    }

    @Override
    public void onDelete(String username) {
        boolean deleted = dbHelper.deleteUser(username);
        if (deleted) {
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
            refreshUserList();
        } else {
            Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdate(String username, String newRole) {
        boolean updated = dbHelper.updateUserRole(username, newRole);
        if (updated) {
            Toast.makeText(this, "User role updated", Toast.LENGTH_SHORT).show();
            refreshUserList();
        } else {
            Toast.makeText(this, "Failed to update user role", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshUserList() {
        ArrayList<User> users = dbHelper.getAllUsersList();
        userAdapter.updateData(users);
    }
}
