package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Get the order ID from the Intent
        String orderId = getIntent().getStringExtra("orderId");

        // Find and set the order details TextView
        TextView tvOrderDetails = findViewById(R.id.tvOrderDetails);
        tvOrderDetails.setText("Order Details for Order ID: " + orderId);

        // Additional logic for displaying order details can go here
    }
}
