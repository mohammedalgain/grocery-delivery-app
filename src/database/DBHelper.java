package com.example.grocerydeliveryapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "grocery_app.db";
    private static final int DATABASE_VERSION = 6;

    // Table Definitions
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String COLUMN_PRODUCT_QUANTITY = "quantity";

    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "id";
    private static final String COLUMN_ORDER_PRODUCT_ID = "product_id";
    private static final String COLUMN_ORDER_QUANTITY = "quantity";
    private static final String COLUMN_ORDER_STATUS = "status";
    private static final String COLUMN_ORDER_DATE = "order_date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create Users Table
            db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_ROLE + " TEXT NOT NULL)");

            // Create Products Table
            db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_PRICE + " REAL NOT NULL, " +
                    COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL)");

            // Create Orders Table
            db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_PRODUCT_ID + " INTEGER NOT NULL, " +
                    COLUMN_ORDER_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_ORDER_STATUS + " TEXT NOT NULL DEFAULT 'Pending', " +
                    COLUMN_ORDER_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "))");

            insertAdminUser(db);

        } catch (Exception e) {
            Log.e(TAG, "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }

    private void insertAdminUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "admin123"); // Use encrypted passwords in production
        values.put(COLUMN_ROLE, "Admin");
        db.insert(TABLE_USERS, null, values);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT o.id AS order_id, o.product_id, o.quantity, o.status, o.order_date, " +
                    "p.name AS product_name, (p.price * o.quantity) AS total_price " +
                    "FROM " + TABLE_ORDERS + " o " +
                    "LEFT JOIN " + TABLE_PRODUCTS + " p ON o.product_id = p.id";

            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                @SuppressLint("Range") double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));

                orders.add(new Order(
                        id,
                        productId,
                        productName != null ? productName : "Unknown",
                        quantity,
                        status,
                        orderDate,
                        totalPrice
                ));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching orders: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return orders;
    }

    public boolean placeOrder(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            Log.e(TAG, "Invalid product or quantity.");
            return false;
        }

        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            if (product.getQuantity() < quantity) {
                Log.e(TAG, "Insufficient stock for product: " + product.getName());
                return false;
            }

            ContentValues orderValues = new ContentValues();
            orderValues.put(COLUMN_ORDER_PRODUCT_ID, product.getId());
            orderValues.put(COLUMN_ORDER_QUANTITY, quantity);
            orderValues.put(COLUMN_ORDER_STATUS, "Pending");

            long orderId = db.insert(TABLE_ORDERS, null, orderValues);

            if (orderId == -1) {
                Log.e(TAG, "Failed to insert order for product: " + product.getName());
                return false;
            }

            int newQuantity = product.getQuantity() - quantity;
            ContentValues productValues = new ContentValues();
            productValues.put(COLUMN_PRODUCT_QUANTITY, newQuantity);
            int rowsUpdated = db.update(TABLE_PRODUCTS, productValues, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});

            if (rowsUpdated == 0) {
                Log.e(TAG, "Failed to update product quantity for product: " + product.getName());
                return false;
            }

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error placing order: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    // Add or Update a Product
    public boolean addOrUpdateProduct(Product product) {
        if (product == null) {
            Log.e(TAG, "Invalid product object.");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(COLUMN_PRODUCT_QUANTITY, product.getQuantity());

        try {
            if (product.getId() != -1) { // Update product
                int rowsUpdated = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getId())});
                return rowsUpdated > 0;
            } else { // Add new product
                long newRowId = db.insert(TABLE_PRODUCTS, null, values);
                return newRowId != -1;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in addOrUpdateProduct: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Check User Credentials
    public boolean checkUser(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{user, pass});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "Error checking user: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // Add a New User
    public boolean addUser(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            Log.e(TAG, "Invalid user details.");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);

        try {
            long newRowId = db.insert(TABLE_USERS, null, values);
            return newRowId != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error adding user: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Get User Role
    public String getUserRole(String user) {
        String role = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
            cursor = db.rawQuery(query, new String[]{user});
            if (cursor.moveToFirst()) {
                role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching user role: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return role != null ? role : "Unknown";
    }

    // Get All Products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_QUANTITY));
                products.add(new Product(id, name, price, quantity));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching products: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return products;
    }

    // Get All Users
    public ArrayList<User> getAllUsersList() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT " + COLUMN_USERNAME + ", " + COLUMN_ROLE + " FROM " + TABLE_USERS, null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));
                users.add(new User(username, role));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching users: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return users;
    }

    // Delete User
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsAffected = db.delete(TABLE_USERS, COLUMN_USERNAME + " = ?", new String[]{username});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting user: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Update User Role
    public boolean updateUserRole(String username, String newRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROLE, newRole);

        try {
            int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating user role: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Check if User Exists
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
            return cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "Error checking if user exists: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

}
