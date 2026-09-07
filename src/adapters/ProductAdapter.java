package com.example.grocerydeliveryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final OnProductSelectListener onProductSelectListener;

    // Constructor to initialize products and the listener
    public ProductAdapter(List<Product> products, OnProductSelectListener onProductSelectListener) {
        this.products = products;
        this.onProductSelectListener = onProductSelectListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.tvProductQuantity.setText(String.format("Available: %d", product.getQuantity()));

        holder.btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product); // Add product to the cart
            Toast.makeText(holder.itemView.getContext(), product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductQuantity;
        Button btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    public interface OnProductSelectListener {
        void onProductSelect(Product product);
    }
}
