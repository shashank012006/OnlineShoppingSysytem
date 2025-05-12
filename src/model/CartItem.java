package model;

public class CartItem {
    private int cartId;
    private int productId;
    private String productName;
    private double price;
    private int quantity;

    // Constructors
    public CartItem() {
    }

    // Getters
    public int getCartId() {
        return cartId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Additional methods
    public double getTotalPrice() {
        return price * quantity;
    }
}
