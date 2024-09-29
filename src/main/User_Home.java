package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import java.util.List;

public class User_Home {
    private TableView<Product> productList;
    private Spinner<Integer> quantitySpinner;
    private Button addToCartButton;
    private Stage stage;
    private Product selectedProduct;
    private Label productDetailLabel;
    private Label productNameLabel;
    private Label productPriceLabel;
    private TextArea productDescriptionTextArea;
    private Label totalLabel;

    @SuppressWarnings({ "deprecation" })
    public void initialize(Stage primaryStage, HomeController homeController) {
        this.stage = primaryStage;
        primaryStage.setTitle("seruput teh");
        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = createMenuBar(primaryStage, homeController);
        productDetailLabel = new Label("SeRuput Teh");
        productDetailLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 40));
        productNameLabel = new Label("Name: ");
        productPriceLabel = new Label("Price: ");
        quantitySpinner = new Spinner<>(1, 10, 1);

        productDescriptionTextArea = new TextArea();
        productDescriptionTextArea.setEditable(false);
        productDescriptionTextArea.setWrapText(true);
        productDescriptionTextArea.setPrefWidth(300);
        productDescriptionTextArea.setPrefHeight(150);

        addToCartButton = new Button("Add to Cart");
        styleaddToCartButton(addToCartButton);
        addToCartButton.setOnAction(e -> homeController.addToCart(selectedProduct, getQuantitySpinner().getValue()));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(productDetailLabel, 0, 0, 2, 1);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(productNameLabel, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(productPriceLabel, 1, 2);
        grid.add(new Label("Description:"), 0, 3);
        grid.add(productDescriptionTextArea, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(quantitySpinner, 1, 4);
        grid.add(addToCartButton, 0, 5, 2, 1);

        productList = new TableView<>();
        productList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productList.getColumns().add(nameColumn);

        productList.setOnMouseClicked(e -> {
            selectedProduct = productList.getSelectionModel().getSelectedItem();
            homeController.showProductDetails(selectedProduct);
            updateTotalLabel();
        });

        GridPane.setMargin(productDetailLabel, new Insets(0, 0, 20, 0));

        totalLabel = new Label("Total: Rp.0.0");
        GridPane.setMargin(totalLabel, new Insets(10, 0, 0, 0));
        grid.add(totalLabel, 0, 6, 2, 1);
        
        quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateTotalLabel();
        });

        borderPane.setTop(menuBar);
        borderPane.setLeft(productList);
        borderPane.setCenter(grid);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    private void updateTotalLabel() {
        if (selectedProduct != null) {
            int quantity = getQuantitySpinner().getValue();
            double totalPrice = selectedProduct.getProductPrice() * quantity;
            totalLabel.setText("Total: Rp." + totalPrice);
        } else {
            totalLabel.setText("Total: Rp.0.0");
        }
    }

    private void styleaddToCartButton(Button checkoutButton) {
        checkoutButton.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");
    }

    private MenuBar createMenuBar(Stage primaryStage, HomeController homeController) {
        MenuBar menuBar = new MenuBar();
        Menu homeMenu = new Menu("Home");
        MenuItem homeItem = new MenuItem("Home Page");
        homeMenu.getItems().add(homeItem);
        homeMenu.setDisable(true);

        Menu cartMenu = new Menu("Cart");
        MenuItem cartItem = new MenuItem("My Cart");
        cartItem.setOnAction(e -> {
            homeController.navigateToCart();
        });
        cartMenu.getItems().add(cartItem);

       
  
        
        

        Menu accountMenu = new Menu("Account");
        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(e -> {
            Stage currentStage = (Stage) productList.getScene().getWindow();
            currentStage.close();
            Login loginView = new Login();
            LoginController loginController = new LoginController(loginView);
            loginController.start(new Stage());
        });
        accountMenu.getItems().add(logoutItem);

        menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);
        return menuBar;
    }

    public void setProductListData(List<Product> data) {
        ObservableList<Product> observableData = FXCollections.observableArrayList(data);
        productList.setItems(observableData);
    }

    public Spinner<Integer> getQuantitySpinner() {
        return quantitySpinner;
    }

    public void close() {
        stage.close();
    }

    public void showProductDetails(Product selectedProduct) {
        if (selectedProduct != null) {
            productNameLabel.setText(selectedProduct.getProductName());
            productPriceLabel.setText("Rp." + selectedProduct.getProductPrice());
            productDescriptionTextArea.setText(selectedProduct.getProductDescription());
        } else {
            productNameLabel.setText("Name: ");
            productPriceLabel.setText("Price: ");
            productDescriptionTextArea.setText("");
            totalLabel.setText("Total: $0.0");
        }
    }
}
