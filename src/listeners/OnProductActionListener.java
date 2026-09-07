package com.example.grocerydeliveryapp;

public interface OnProductActionListener {
    /**
     * Called when a product needs to be edited.
     *
     * @param product The product to be edited.
     */
    void onEdit(Product product);

    /**
     * Called when a product needs to be deleted.
     *
     * @param product The product to be deleted.
     */
    void onDelete(Product product);

    /**
     * Called when a product is selected (e.g., for viewing or adding to cart).
     *
     * @param product The product that was selected.
     */
    void onSelect(Product product);
}
