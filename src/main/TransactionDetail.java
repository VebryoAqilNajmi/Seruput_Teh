package main;

public class TransactionDetail {
    private String transactionID;
    private String productID;
    private int quantity;
    private String productName;
    private double productPrice;

    public TransactionDetail() {
    }

    public TransactionDetail(String transactionID, String productID, int quantity) {
        this.transactionID = transactionID;
        this.productID = productID;
        this.quantity = quantity;
        this.productName = "";
        this.productPrice = 0.0;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
