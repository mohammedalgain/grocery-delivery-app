package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView tvTotalPrice;
    private Button btnClearCart;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnClearCart = findViewById(R.id.btnClearCart);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);

        loadCartItems();

        btnClearCart.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            loadCartItems();
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCartItems() {
        List<Product> cartItems = CartManager.getInstance().getCartItems();

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            tvTotalPrice.setText("Total: $0.00");
            recyclerViewCart.setAdapter(null);
            return;
        }

        CartAdapter cartAdapter = new CartAdapter(cartItems);
        recyclerViewCart.setAdapter(cartAdapter);

        double total = CartManager.getInstance().calculateTotal();
        tvTotalPrice.setText(String.format("Total: $%.2f", total));
    }
}
