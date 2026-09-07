package com.example.grocerydeliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductSelectListener {

    private RecyclerView rvProducts;
    private Button btnAddProduct;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        // Initialize views
        rvProducts = findViewById(R.id.rvProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        dbHelper = new DBHelper(this);

        // Set up RecyclerView
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        // Handle Add Product button click
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ManageProductsActivity.this, AddEditProductActivity.class);
            startActivity(intent);
        });
    }

    // Method to load products into the RecyclerView
    private void loadProducts() {
        List<Product> products = dbHelper.getAllProducts();
        ProductAdapter productAdapter = new ProductAdapter(products, this);
        rvProducts.setAdapter(productAdapter);
    }

    // Handle product selection (Edit/Delete)
    @Override
    public void onProductSelect(Product product) {
        // For example, launch the AddEditProductActivity to edit the product
        Intent intent = new Intent(ManageProductsActivity.this, AddEditProductActivity.class);
        intent.putExtra("product", product); // Ensure Product implements Parcelable or Serializable
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Reload products when returning to this activity
    }
}
