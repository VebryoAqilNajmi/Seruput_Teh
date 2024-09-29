package main;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User_History {

    private TableView<Transaction> transactionTable;
    private TableView<TransactionDetail> transactionDetailTable;
    private Label usernameLabel;
    private Label transactionIdLabel;
    private Label totalLabel;
    private TableColumn<TransactionDetail, String> detailTransactionIdColumn;
    private TableColumn<TransactionDetail, String> productIdColumn;
    private TableColumn<TransactionDetail, String> productNameColumn;
    private TableColumn<TransactionDetail, Integer> quantityColumn;
    private TableColumn<TransactionDetail, Double> totalPriceColumn;
    private Transaction selectedTransaction;
    private User loggedInUser;

    @SuppressWarnings("deprecation")
    public void initialize(HistoryController historyController, String username, User loggedInUser) {
        this.loggedInUser = loggedInUser;
        BorderPane root = new BorderPane();

        usernameLabel = new Label(username + "'s Transaction(s)");
        transactionIdLabel = new Label();
        totalLabel = new Label("Total Price: ");

        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        root.setTop(createMenuBar(historyController));

        transactionTable = new TableView<>();
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        createTransactionDetailTable();

        HBox labelsBox = new HBox(10, usernameLabel, totalLabel);
        labelsBox.setPadding(new Insets(0, 50, 0, 0)); 

        HBox hbox = new HBox(10, transactionTable, transactionDetailTable);
        hbox.setPadding(new Insets(10));

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(labelsBox, hbox, transactionIdLabel);

        root.setCenter(vBox);

        Scene scene = new Scene(root, 650, 600);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("seruput teh");

        primaryStage.setOnCloseRequest(event -> historyController.close());

        primaryStage.show();
    }


    private MenuBar createMenuBar(HistoryController historyController) {
        MenuBar menuBar = new MenuBar();
        
        Menu homeMenu = new Menu("Home");
        MenuItem homeItem = new MenuItem("Home Page");
        homeItem.setOnAction(e -> historyController.navigateToHome(loggedInUser));
        homeMenu.getItems().add(homeItem);

        Menu cartMenu = new Menu("Cart");
        MenuItem cartItem = new MenuItem("My Cart");
        cartItem.setOnAction(e -> historyController.navigateToCart());
        cartMenu.getItems().add(cartItem);

        Menu historyMenu = new Menu("History");
        historyMenu.setDisable(true);

        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            Stage currentStage = (Stage) usernameLabel.getScene().getWindow();
            currentStage.close();
            Login loginView = new Login();
            LoginController loginController = new LoginController(loginView);
            loginController.start(new Stage());
        });
        accountMenu.getItems().add(logoutItem);

        menuBar.getMenus().addAll(homeMenu, cartMenu, historyMenu, accountMenu);

        return menuBar;
    }


    @SuppressWarnings({ "unchecked", "deprecation" })
    private void createTransactionDetailTable() {
        transactionDetailTable = new TableView<>();
        transactionDetailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        transactionDetailTable.getColumns().addAll(
                getDetailTransactionIdColumn(),
                getProductIdColumn(),
                getProductNameColumn(),
                getQuantityColumn(),
                getTotalPriceColumn()
        );
    }

    public TableView<Transaction> getTransactionTable() {
        return transactionTable;
    }

    public TableView<TransactionDetail> getTransactionDetailTable() {
        return transactionDetailTable;
    }

    public Label getTransactionIdLabel() {
        return transactionIdLabel;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public void show(HistoryController historyController, String username, User loggedInUser) {
        initialize(historyController, username, loggedInUser);
    }

    public void close() {
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }

    public void showTransactionDetails(List<TransactionDetail> transactionDetails) {
        transactionDetailTable.getItems().clear();
        transactionDetailTable.getItems().addAll(transactionDetails);
        calculateTotalPrice(transactionDetails);

        updateTotalPriceLabel(selectedTransaction);
    }

    private double calculateTotalPrice(List<TransactionDetail> transactionDetails) {
        double totalPrice = 0.0;

        for (TransactionDetail transactionDetail : transactionDetails) {
            double productPrice = transactionDetail.getProductPrice();
            int quantity = transactionDetail.getQuantity();
            double totalProductPrice = productPrice * quantity;
            totalPrice += totalProductPrice;
        }

        return totalPrice;
    }

    public void showTotalPrice(double totalPrice) {
        totalLabel.setText(String.format("Total Price: %.2f", totalPrice));
    }

    private TableColumn<TransactionDetail, String> getDetailTransactionIdColumn() {
        if (detailTransactionIdColumn == null) {
            detailTransactionIdColumn = new TableColumn<>("Transaction ID");
            detailTransactionIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTransactionID()));
        }
        return detailTransactionIdColumn;
    }

    private TableColumn<TransactionDetail, String> getProductIdColumn() {
        if (productIdColumn == null) {
            productIdColumn = new TableColumn<>("Product ID");
            productIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductID()));
        }
        return productIdColumn;
    }

    private TableColumn<TransactionDetail, String> getProductNameColumn() {
        if (productNameColumn == null) {
            productNameColumn = new TableColumn<>("Product Name");
            productNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        }
        
        return productNameColumn;
    }

    private TableColumn<TransactionDetail, Integer> getQuantityColumn() {
        if (quantityColumn == null) {
            quantityColumn = new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        }
        return quantityColumn;
    }

    private TableColumn<TransactionDetail, Double> getTotalPriceColumn() {
        if (totalPriceColumn == null) {
            totalPriceColumn = new TableColumn<>("Total Price");
            totalPriceColumn.setCellValueFactory(data -> {
                double totalProductPrice = calculateTotalPrice(List.of(data.getValue()));
                return new SimpleDoubleProperty(totalProductPrice).asObject();
            });
        }
        return totalPriceColumn;
    }

    private void updateTotalPriceLabel(Transaction selectedTransaction) {
        if (selectedTransaction != null) {
            double totalPriceFromDatabase = getTotalPriceFromDatabase(selectedTransaction);

            Platform.runLater(() -> {
                showTotalPrice(totalPriceFromDatabase);
            });
        }
    }

    public double getTotalPriceFromDatabase(Transaction selectedTransaction) {
        double totalPriceFromDatabase = 0.0;

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(
            		 "SELECT SUM(td.Quantity * h.product_price) AS TotalPrice " +
            				 "FROM transaction_detail td " +
            				 "JOIN product h ON td.ProductID = h.productID " +
            				 "WHERE td.TransactionID = ?")) {

            statement.setString(1, selectedTransaction.getTransactionID());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalPriceFromDatabase = resultSet.getDouble("TotalPrice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPriceFromDatabase;
    }

}

