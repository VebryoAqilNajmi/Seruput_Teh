package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    public List<Transaction> getTransactionsByUserID(String userID) {
        List<Transaction> userTransactions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM transaction_header WHERE userID = ?")) {

            preparedStatement.setString(1, userID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionID(resultSet.getString("transactionID"));
                    transaction.setUserID(resultSet.getString("userID"));
                    userTransactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userTransactions;
    }

    public List<TransactionDetail> getTransactionDetailsByTransactionID(String transactionID) {
        List<TransactionDetail> details = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM transaction_detail WHERE transactionID = ?")) {

            preparedStatement.setString(1, transactionID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TransactionDetail transactionDetail = new TransactionDetail();
                    transactionDetail.setTransactionID(resultSet.getString("transactionID"));
                    transactionDetail.setProductID(resultSet.getString("productID"));
                    transactionDetail.setQuantity(resultSet.getInt("quantity"));
                    details.add(transactionDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return details;
    }

    public List<TransactionDetail> getTransactionDetails(String transactionID) {
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT td.transactionID, td.productID, td.quantity, p.product_name, p.product_price " +
                     "FROM transaction_detail td " +
                     "JOIN product p ON td.productID = p.productID " +
                     "WHERE td.transactionID = ?")) {

            preparedStatement.setString(1, transactionID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TransactionDetail transactionDetail = new TransactionDetail();
                    transactionDetail.setTransactionID(resultSet.getString("transactionID"));
                    transactionDetail.setProductID(resultSet.getString("productID"));
                    transactionDetail.setQuantity(resultSet.getInt("quantity"));
                    transactionDetail.setProductName(resultSet.getString("product_name"));
                    double productPrice = resultSet.getDouble("product_price");
                    transactionDetail.setProductPrice(productPrice);
                    transactionDetails.add(transactionDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionDetails;
    }

    public double getProductPriceById(String productID) {
        double productPrice = 0.0;

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT product_price FROM product WHERE productID = ?")) {

            preparedStatement.setString(1, productID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    productPrice = resultSet.getDouble("product_price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productPrice;
    }
}
