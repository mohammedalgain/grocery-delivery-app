package com.example.grocerydeliveryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users;
    private final UserActionListener listener;

    public UserAdapter(ArrayList<User> users, UserActionListener listener, ManageUsersActivity manageUsersActivity) {
        this.users = users != null ? users : new ArrayList<>(); // Initialize to an empty list if null
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        // Set user data
        holder.tvUsername.setText(user.getUsername());
        holder.tvRole.setText(user.getRole());

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(user.getUsername());
            }
        });

        // Update role button click
        holder.btnUpdate.setOnClickListener(v -> {
            if (listener != null) {
                // Toggle role between "Admin" and "User"
                String newRole = user.getRole().equalsIgnoreCase("Admin") ? "User" : "Admin";
                listener.onUpdate(user.getUsername(), newRole);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Method to update adapter data and refresh the RecyclerView
    public void updateData(ArrayList<User> updatedUsers) {
        if (updatedUsers == null) {
            this.users = new ArrayList<>(); // Set to an empty list if null
        } else {
            this.users = updatedUsers;
        }
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvRole;
        Button btnDelete, btnUpdate;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRole = itemView.findViewById(R.id.tvRole);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
