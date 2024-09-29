package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    public List<Cart> getCartItemsByUserID(String userID) {
        List<Cart> cartItems = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Cart WHERE UserID = ?")) {

            preparedStatement.setString(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Cart cartItem = new Cart();
                    cartItem.setUserID(resultSet.getString("UserID"));
                    cartItem.setProductID(resultSet.getString("ProductID"));
                    cartItem.setQuantity(resultSet.getInt("Quantity"));
                    Product product = getProductByID(cartItem.getProductID());
                    cartItem.setProductName(product.getProductName());
                    cartItem.setProductPrice(product.getProductPrice());
                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    private Product getProductByID(String productID) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Product WHERE ProductID = ?")) {

            preparedStatement.setString(1, productID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setProductID(resultSet.getString("ProductID"));
                    product.setProductName(resultSet.getString("Product_name"));
                    product.setProductPrice(resultSet.getLong("Product_price"));

                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addToCart(String userID, String hoodieID, int quantity) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Cart (UserID, ProductID, Quantity) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, hoodieID);
            preparedStatement.setInt(3, quantity);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(String userID, String hoodieID) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM Cart WHERE UserID = ? AND ProductID = ?")) {

            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, hoodieID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTransactionIndex() {
        try (Connection connection = DatabaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(TransactionID) FROM transaction_header")) {

            if (resultSet.next()) {
                String maxTransactionID = resultSet.getString(1);
                if (maxTransactionID != null) {
                    int transactionIndex = Integer.parseInt(maxTransactionID.substring(2));
                    return transactionIndex + 1;
                } else {
                    return 1; 
                }
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; 
        }
    }


  
    public boolean clearCart(String userID) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM Cart WHERE UserID = ?")) {

            preparedStatement.setString(1, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean insertTransaction(String userID, String transactionID, List<Cart> cartItems) {
        try (Connection connection = DatabaseConnection.connect()) {
            String insertTransactionHeaderSQL = "INSERT INTO transaction_header (TransactionID, UserID) VALUES (?, ?)";
            try (PreparedStatement transactionHeaderStatement = connection.prepareStatement(insertTransactionHeaderSQL)) {
                transactionHeaderStatement.setString(1, transactionID);
                transactionHeaderStatement.setString(2, userID);
                transactionHeaderStatement.executeUpdate();
            }

            String insertTransactionDetailSQL = "INSERT INTO transaction_detail (TransactionID, ProductID, Quantity) VALUES (?, ?, ?)";
            try (PreparedStatement transactionDetailStatement = connection.prepareStatement(insertTransactionDetailSQL)) {
                for (Cart cartItem : cartItems) {
                    transactionDetailStatement.setString(1, transactionID);
                    transactionDetailStatement.setString(2, cartItem.getProductID());
                    transactionDetailStatement.setInt(3, cartItem.getQuantity());

                    transactionDetailStatement.executeUpdate();
                }
            }

            String clearCartSQL = "DELETE FROM cart WHERE UserID = ?";
            try (PreparedStatement clearCartStatement = connection.prepareStatement(clearCartSQL)) {
                clearCartStatement.setString(1, userID);
                clearCartStatement.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateCart(String userID, String productID, int newQuantity) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Cart SET Quantity = ? WHERE UserID = ? AND ProductID = ?")) {

            preparedStatement.setInt(1, newQuantity);
            preparedStatement.setString(2, userID);
            preparedStatement.setString(3, productID);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
