package com.example.grocerydeliveryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditProductActivity extends AppCompatActivity {

    private EditText etProductName, etProductPrice, etProductQuantity;
    private Button btnSaveProduct;
    private DBHelper dbHelper;
    private int productId = -1; // -1 means adding a new product

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize views
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        // Check if editing a product
        if (getIntent().hasExtra("product")) {
            Product product = (Product) getIntent().getSerializableExtra("product");
            if (product != null) {
                productId = product.getId(); // Fetch the product ID for updates
                etProductName.setText(product.getName());
                etProductPrice.setText(String.valueOf(product.getPrice()));
                etProductQuantity.setText(String.valueOf(product.getQuantity()));
            }
        }

        // Save product button logic
        btnSaveProduct.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String priceStr = etProductPrice.getText().toString().trim();
        String quantityStr = etProductQuantity.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(quantityStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            Product product = new Product(productId, name, price, quantity);

            boolean result;
            if (productId == -1) {
                // Add a new product
                result = dbHelper.addOrUpdateProduct(product);
                showToast(result, "added");
            } else {
                // Update existing product
                result = dbHelper.addOrUpdateProduct(product);
                showToast(result, "updated");
            }

            if (result) {
                finish(); // Close activity on success
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or quantity. Please enter valid numbers.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast(boolean success, String action) {
        if (success) {
            Toast.makeText(this, "Product " + action + " successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to " + action + " product", Toast.LENGTH_SHORT).show();
        }
    }
}
