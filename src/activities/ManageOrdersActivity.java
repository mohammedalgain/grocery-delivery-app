package com.example.grocerydeliveryapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrdersAdapter ordersAdapter;
    private List<Order> ordersList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        // Initialize views
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Load orders from the database
        loadOrders();
    }

    /**
     * Loads the orders from the database and displays them in the RecyclerView.
     */
    private void loadOrders() {
        ordersList = dbHelper.getAllOrders();

        if (ordersList.isEmpty()) {
            Log.d(TAG, "No orders to display");
            Toast.makeText(this, "No orders available", Toast.LENGTH_SHORT).show();
        } else {
            ordersAdapter = new OrdersAdapter(ordersList, order -> {
                Toast.makeText(this, "Order ID: " + order.getId(), Toast.LENGTH_SHORT).show();
            });
            recyclerViewOrders.setAdapter(ordersAdapter);
            ordersAdapter.notifyDataSetChanged();
        }

    }
}
