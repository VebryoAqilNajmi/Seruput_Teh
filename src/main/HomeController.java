package main;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class HomeController {
    private User_Home homeView;
    private ProductManager productManager;
    private CartManager cartManager;
    private TransactionManager transactionManager;
    private User loggedInUser;

    public HomeController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.productManager = new ProductManager();
        this.cartManager = new CartManager();
        this.transactionManager = new TransactionManager();
    }

    public void setHomeView(User_Home homeView) {
        this.homeView = homeView;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("seruput teh");

        homeView = new User_Home();
        homeView.initialize(primaryStage, this);

        homeView.setProductListData(productManager.getAllProducts());

        primaryStage.show();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void addToCart(Product selectedProduct, int quantity) {
        String userID = loggedInUser.getUserID();

        boolean addToCartSuccess = cartManager.addToCart(userID, selectedProduct.getProductID(), quantity);

        if (addToCartSuccess) {
            showAlert("Success", "Product added to cart successfully!");
        } else {
            showAlert("Error", "Failed to add product to cart. Please try again.");
        }
    }

    public void showProductDetails(Product selectedProduct) {
        homeView.showProductDetails(selectedProduct);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateToCart() {
        User_Cart cartView = new User_Cart();
        CartController cartController = new CartController(loggedInUser);
        cartController.setCartView(cartView);
        Stage cartStage = new Stage();
        cartController.start(cartStage);
        homeView.close();
    }

    public void navigateToHistory() {
        HistoryController historyController = new HistoryController(transactionManager, loggedInUser.getUserID(), loggedInUser);
        historyController.start();
        homeView.close();
    }
}
