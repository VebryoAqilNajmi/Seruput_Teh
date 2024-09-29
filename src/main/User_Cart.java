package main;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class User_Cart {
    private TableView<Cart> cartTable;
    private Label usernameLabel;
    private Label productDetailLabel;
    private Label productNameLabel;
    private Label productPriceLabel;
    private Spinner<Integer> quantitySpinner;
    private Label totalPriceLabel;
    private Button removeFromCartButton;
    private Button updateCartButton;
    private Label orderInformationLabel;
    private Label usernameOrderLabel;
    private Label phoneLabel;
    private Label addressLabel;
    private Label cartTotalPriceLabel;
    private User loggedInUser;
    private double totalCartPrice = 0.0;

    @SuppressWarnings({ "unchecked", "deprecation" })
    public void initialize(Stage primaryStage, CartController cartController, User loggedInUser) {
        this.loggedInUser = loggedInUser;
        primaryStage.setTitle("seruput teh");

        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = createMenuBar(primaryStage, cartController);

        usernameLabel = new Label("Username: " + loggedInUser.getUsername());
        usernameLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 16));

        cartTable = new TableView<>();
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Cart, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<Cart, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Cart, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        cartTable.getColumns().addAll(nameColumn, quantityColumn, totalPriceColumn);

        cartTable.setOnMouseClicked(event -> {
            Cart selectedItem = cartTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cartController.showCartItemDetails(selectedItem);
            }
        });

        productDetailLabel = new Label("");
        productNameLabel = new Label("");
        productPriceLabel = new Label("");
        quantitySpinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setVisible(false);
        quantitySpinner.getValueFactory().setValue(1);
        totalPriceLabel = new Label("");
        removeFromCartButton = new Button("Remove from Cart");
        styleRemoveFromCartButton();
        removeFromCartButton.setPrefWidth(150); // Tentukan lebar yang diinginkan
        removeFromCartButton.setPrefHeight(40);
        removeFromCartButton.setOnAction(e -> {
            Cart selectedItem = cartTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cartController.removeFromCart(selectedItem);
                clearCartItemDetails();
            }
        });
        updateCartButton = new Button("Update Cart");
        updateCartButton.setOnAction(e -> updateCart(cartController));
        styleCheckoutButton(updateCartButton);
        updateCartButton.setPrefWidth(150); // Tentukan lebar yang diinginkan
        updateCartButton.setPrefHeight(40);

        productDetailLabel.setText("Product's Detail:");
        productDetailLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 18));
        productNameLabel.setText("");
        productPriceLabel.setText("");
        totalPriceLabel.setText("");

        orderInformationLabel = new Label("Order Information:");
        orderInformationLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 18));
        usernameOrderLabel = new Label("Username: " + loggedInUser.getUsername());
        phoneLabel = new Label("");
        addressLabel = new Label("");

        cartTotalPriceLabel = new Label("Cart Total Price: 0.0");
        cartTotalPriceLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 20));
        
        HBox quantityHBox = new HBox(10, new Label("Qty:"), quantitySpinner);
        quantityHBox.setVisible(false);

        cartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                quantitySpinner.setVisible(true);
                quantityHBox.setVisible(true);
                quantitySpinner.getValueFactory().setValue(newSelection.getQuantity());
            } else {
                quantitySpinner.setVisible(false);
            }
        });

        VBox productDetailsVBox = new VBox(10,
                productDetailLabel, productNameLabel,
                productPriceLabel, quantityHBox,
                totalPriceLabel, removeFromCartButton, updateCartButton);

        VBox orderInfoVBox = new VBox(10, orderInformationLabel, usernameOrderLabel, phoneLabel, addressLabel);
        VBox cartTotalPriceVBox = new VBox(10, cartTotalPriceLabel);
        VBox.setMargin(cartTotalPriceVBox, new Insets(0, 0, 10, 175));

        VBox checkoutButtonVBox = createCheckoutButtonVBox(cartController);

        HBox detailsAndOrderInfoHBox = new HBox(20, productDetailsVBox, orderInfoVBox);
        HBox.setMargin(orderInfoVBox, new Insets(20, 0, 0, 150));
        HBox.setMargin(productDetailsVBox, new Insets(20, 0, 0, 50));
        VBox mainVBox = new VBox(20, detailsAndOrderInfoHBox, cartTotalPriceVBox, checkoutButtonVBox, cartTable);

        VBox.setMargin(usernameLabel, new Insets(0, 0, 20, 0));

        borderPane.setTop(menuBar);
        borderPane.setCenter(mainVBox);

        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setScene(scene);
    }

    private VBox createCheckoutButtonVBox(CartController cartController) {
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> cartController.checkoutAction());
        checkoutButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: white;");


        VBox checkoutButtonVBox = new VBox(10, checkoutButton);
        checkoutButtonVBox.setAlignment(Pos.CENTER);

        return checkoutButtonVBox;
    }

    private void styleRemoveFromCartButton() {
        removeFromCartButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
    }

    private void styleCheckoutButton(Button checkoutButton) {
        checkoutButton.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");
    }

    private MenuBar createMenuBar(Stage primaryStage, CartController cartController) {
        MenuBar menuBar = new MenuBar();

        Menu homeMenu = new Menu("Home");
        MenuItem homeItem = new MenuItem("Home Page");
        homeItem.setOnAction(e -> cartController.navigateToHome());
        homeMenu.getItems().add(homeItem);

        Menu cartMenu = new Menu("Cart");
        MenuItem cartItem = new MenuItem("My Cart");
        cartMenu.getItems().add(cartItem);
        cartMenu.setDisable(true);

        Menu historyMenu = new Menu("Purchase History");
        MenuItem historyItem = new MenuItem("Transaction History");
        historyItem.setOnAction(event -> cartController.navigateToHistory(loggedInUser));
        historyMenu.getItems().add(historyItem);

        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> cartController.logout());
        accountMenu.getItems().add(logoutItem);

        menuBar.getMenus().addAll(homeMenu, cartMenu, historyMenu, accountMenu);

        return menuBar;
    }

    public void setCartData(ObservableList<Cart> data) {
        cartTable.setItems(data);
        updateCartTotalPrice();
    }

    public void showCartItemDetails(Cart selectedItem) {
        productNameLabel.setText("Name: " + selectedItem.getProductName());
        productPriceLabel.setText("Price: " + selectedItem.getProductPrice());
        quantitySpinner.getValueFactory().setValue(selectedItem.getQuantity());
        totalPriceLabel.setText("Total Price: " + selectedItem.getTotalPrice());
        removeFromCartButton.setDisable(false);
    }

    public void clearCartItemDetails() {
        productNameLabel.setText("");
        productPriceLabel.setText("");
        quantitySpinner.getValueFactory().setValue(1);
        totalPriceLabel.setText("");
        removeFromCartButton.setDisable(true);
        updateCartTotalPrice();
    }

    public void setContactInformation(String phone, String address) {
        phoneLabel.setText("Phone: " + phone);
        addressLabel.setText("Address: " + address);
    }

    public void setCartTotalPrice(double totalPrice) {
        totalCartPrice = totalPrice;
        cartTotalPriceLabel.setText("Cart Total Price: " + totalCartPrice);
    }

    public void setCartItems(ObservableList<Cart> data) {
        cartTable.setItems(data);
    }

    public ObservableList<Cart> getCartItems() {
        return cartTable.getItems();
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        if (usernameLabel != null) {
            usernameLabel.setText("Username: " + loggedInUser.getUsername());
        }
    }

    private void updateCartTotalPrice() {
        ObservableList<Cart> cartItems = getCartItems();
        double totalPrice = 0.0;

        for (Cart cartItem : cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }

        setCartTotalPrice(totalPrice);
    }

    public void close() {
        Stage stage = (Stage) cartTable.getScene().getWindow();
        stage.close();
    }

    private void updateCart(CartController cartController) {
        Cart selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            int newQuantity = quantitySpinner.getValue();
            selectedItem.setQuantity(newQuantity);
            cartController.updateCart(selectedItem);
            clearCartItemDetails();
        }
    }
}
