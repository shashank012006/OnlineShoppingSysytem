package model;

public class CartItem {
    // private int cartId;
    // private int productId;
    // private String productName;
    private double price;
    private int quantity;

    // Constructors, getters, and setters
    public CartItem() {
    }

    // Add all getters and setters here
    // ...

    public double getTotalPrice() {
        return price * quantity;
    }

    public void setCartId(int int1) {
        throw new UnsupportedOperationException("Unimplemented method 'setCartId'");
    }

    public void setPrice(double double1) {
        throw new UnsupportedOperationException("Unimplemented method 'setPrice'");
    }

    public void setProductName(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'setProductName'");
    }

    public void setQuantity(int int1) {
        throw new UnsupportedOperationException("Unimplemented method 'setQuantity'");
    }

    public Object getProductName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductName'");
    }

    public Object getCartId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartId'");
    }

    public Object getPrice() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPrice'");
    }

    public Object getQuantity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQuantity'");
    }

    public int getProductId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductId'");
    }
}