package main;

public class Cart {
    private String userID;
    private String productID;
    private int quantity;

    private String productName;
    private long productPrice;

    public Cart() {
    }

    public Cart(String userID, String productID, int quantity) {
        this.userID = userID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getUserID() {
        return userID;
    }

    public String getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public long getTotalPrice() {
        return quantity * productPrice;
    }
}
