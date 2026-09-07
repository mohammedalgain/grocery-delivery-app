package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rvProducts = findViewById(R.id.rvProducts);
        dbHelper = new DBHelper(this);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();
    }

    private void loadProducts() {
        List<Product> productList = dbHelper.getAllProducts();
        ArrayList<Product> products = new ArrayList<>(productList);
        ProductAdapter productAdapter = new ProductAdapter(products, this::onProductSelected);
        rvProducts.setAdapter(productAdapter);
    }

    private void onProductSelected(Product product) {
        Toast.makeText(this, "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
    }
}
