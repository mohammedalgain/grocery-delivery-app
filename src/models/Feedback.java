package com.example.grocerydeliveryapp;

public class Feedback {
    private int productId;
    private String userId;
    private String comment;
    private int rating;

    public Feedback(int productId, String userId, String comment, int rating) {
        this.productId = productId;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public int getProductId() {
        return productId;
    }

    public String getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
