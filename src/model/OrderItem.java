package model;

public class OrderItem {
    // private int itemId;
    // private int orderId;
    // private int productId;
    // private String productName;
    private int quantity;
    private double price;

    // Constructors, getters, and setters
    // ...

    public double getTotalPrice() {
        return price * quantity;
    }

    public void setItemId(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setItemId'");
    }

    public void setOrderId(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setOrderId'");
    }

    public void setProductId(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProductId'");
    }

    public void setProductName(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProductName'");
    }

    public void setQuantity(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setQuantity'");
    }

    public void setPrice(double double1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPrice'");
    }

    public Object getProductName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductName'");
    }

    public Object getQuantity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQuantity'");
    }

    public Object getPrice() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPrice'");
    }
}