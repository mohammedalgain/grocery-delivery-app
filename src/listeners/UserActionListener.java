package com.example.grocerydeliveryapp;

public interface UserActionListener {
    void onDelete(String username); // For deleting users
    void onUpdate(String username, String newRole); // For updating roles
}
