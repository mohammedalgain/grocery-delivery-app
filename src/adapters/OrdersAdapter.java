package com.example.grocerydeliveryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final List<Order> ordersList;
    private final OnOrderClickListener listener;

    // Functional interface for order click listener
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrdersAdapter(List<Order> ordersList, OnOrderClickListener listener) {
        this.ordersList = ordersList != null ? ordersList : List.of(); // Avoid NullPointerException
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);

        // Set order details
        holder.tvOrderId.setText("Order ID: " + order.getId());
        holder.tvStatus.setText("Status: " + (order.getStatus() != null ? order.getStatus() : "Unknown"));
        holder.tvTotalPrice.setText("Total: $" + order.getTotalPrice());

        // Check and display product name
        String productName = order.getProductName() != null ? order.getProductName() : "Unknown";
        holder.tvProductName.setText("Product: " + productName);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList != null ? ordersList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvStatus, tvTotalPrice, tvProductName;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvProductName = itemView.findViewById(R.id.tvProductName); // Initialize product name TextView
        }
    }
}
