package com.example.grocerydeliveryapp;

public class Order {
    private int id;            // معرف الطلب
    private int productId;     // معرف المنتج
    private String productName; // اسم المنتج
    private int quantity;      // الكمية المطلوبة
    private String status;     // حالة الطلب (Pending, Shipped, Delivered)
    private String orderDate;  // تاريخ الطلب
    private double totalPrice; // السعر الإجمالي للطلب

    /**
     * المنشئ الكامل للطلب.
     *
     * @param id        معرف الطلب.
     * @param productId معرف المنتج المرتبط بالطلب.
     * @param quantity  الكمية المطلوبة.
     * @param status    حالة الطلب (Pending, Shipped, Delivered).
     * @param orderDate تاريخ الطلب.
     */
    public Order(int id, int productId, String productName, int quantity, String status, String orderDate, double totalPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName != null ? productName : "Unknown"; // Handle null product name
        this.quantity = quantity;
        this.status = status;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }



    // دوال المساعدة لتجنب التكرار
    private String validateString(String value, String defaultValue) {
        return value != null && !value.trim().isEmpty() ? value : defaultValue;
    }

    private int validateNonNegative(int value) {
        return Math.max(value, 0);
    }

    private double validateNonNegative(double value) {
        return Math.max(value, 0.0);
    }

    // Getters و Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = validateString(productName, "Unknown");
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = validateNonNegative(quantity);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = validateString(status, "Pending");
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate != null ? orderDate : "Not Available";
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = validateNonNegative(totalPrice);
    }

    /**
     * تنسيق نصي لعرض تفاصيل الطلب.
     *
     * @return نص يصف تفاصيل الطلب.
     */
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
