package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getString("productID"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductPrice(resultSet.getLong("product_price"));
                product.setProductDescription(resultSet.getString("product_des"));
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product getProductById(String productId) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE productID = ?")) {

            preparedStatement.setString(1, productId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setProductID(resultSet.getString("productID"));
                    product.setProductName(resultSet.getString("product_name"));
                    product.setProductPrice(resultSet.getLong("product_price"));
                    product.setProductDescription(resultSet.getString("product_des"));
                    return product;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addProductToDatabase(Product product) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO product (productID, product_name, product_price, product_des) VALUES (?, ?, ?, ?)")) {

            String generatedID = generateProductID();
            product.setProductID(generatedID);

            preparedStatement.setString(1, product.getProductID());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setLong(3, product.getProductPrice());
            preparedStatement.setString(4, product.getProductDescription());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String generateProductID() {
        List<Product> allProducts = getAllProducts();
        int lastIndex = allProducts.size() - 1;
        String lastProductID = lastIndex >= 0 ? allProducts.get(lastIndex).getProductID() : null;

        if (lastProductID == null || !lastProductID.startsWith("TE")) {
            return "TE001";
        }

        int lastNumber = Integer.parseInt(lastProductID.substring(2));
        String newNumber = String.format("%03d", lastNumber + 1);
        return "TE" + newNumber;
    }

    public boolean updateProduct(Product product, long newPrice, String newName, String newDescription) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE product SET product_name = ?, product_price = ?, product_des = ? WHERE productID = ?")) {

            preparedStatement.setString(1, newName);
            preparedStatement.setLong(2, newPrice);
            preparedStatement.setString(3, newDescription);
            preparedStatement.setString(4, product.getProductID());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(Product product) {
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM product WHERE productID = ?")) {

            preparedStatement.setString(1, product.getProductID());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
