package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CartController {
    private User_Cart cartView;
    private CartManager cartManager;
    private UserManager userManager;
    private User loggedInUser;
    private Stage primaryStage;

    public CartController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.cartManager = new CartManager();
        this.userManager = new UserManager();
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void setHomeView(User_Home homeView) {
    }

    public void setCartView(User_Cart cartView) {
        this.cartView = cartView;
        cartView.setLoggedInUser(loggedInUser);
    }

    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage; 
        primaryStage.setTitle("Cart Form");

        cartView = new User_Cart();
        cartView.initialize(primaryStage, this, loggedInUser);

        updateCartItems();

        updateContactInformation();

        primaryStage.show();
    }

    public void updateCartItems() {
        String userID = loggedInUser.getUserID();
        ObservableList<Cart> cartItems = FXCollections.observableArrayList(cartManager.getCartItemsByUserID(userID));
        cartView.setCartItems(cartItems);
        updateCartTotalPrice();
    }

    public void updateContactInformation() {
        String userID = loggedInUser.getUserID();
        User user = userManager.getUserByID(userID);
        cartView.setContactInformation(user.getPhoneNum(), user.getAddress());
    }

    public void removeFromCart(Cart selectedCartItem) {
        String userID = loggedInUser.getUserID();
        boolean success = cartManager.removeFromCart(userID, selectedCartItem.getProductID());

        if (success) {
            updateCartItems();
        } else {
            System.out.println("Failed to remove item from cart.");
        }

        updateCartTotalPrice();
    }

    public double calculateTotalPrice() {
        ObservableList<Cart> cartItems = cartView.getCartItems();
        double totalPrice = 0;

        for (Cart cartItem : cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }

        return totalPrice;
    }
    
    public void showCartItemDetails(Cart selectedItem) {
        cartView.showCartItemDetails(selectedItem);
    }
    
    public void logout() {

    }
    
    public void checkoutAction() {
        if (cartView.getCartItems().isEmpty()) {
            showErrorAlert("Error", "Cannot checkout. Cart is empty.");
        } else {
            boolean proceed = User_Checkout.display("Are you sure to make a transaction?");
            if (proceed) {
                boolean paymentSuccess = makePayment();
                if (paymentSuccess) {
                    showSuccessAlert("Payment Successful", "Thank you for your purchase!");
                    clearCartAndNavigateToHistory();
                } else {
                    showErrorAlert("Payment Failed", "Payment was not successful. Please try again.");
                }
            }
        }
    }

    private void clearCartAndNavigateToHistory() {
        String userID = loggedInUser.getUserID();
        cartManager.clearCart(userID); 
        updateCartItems(); 

    }
    
    public void updateCart(Cart selectedItem) {
        String userID = loggedInUser.getUserID();
        boolean success = cartManager.updateCart(userID, selectedItem.getProductID(), selectedItem.getQuantity());

        if (success) {
            updateCartItems();
            cartView.clearCartItemDetails();
        } else {
            System.out.println("Failed to update cart.");
        }

        updateCartTotalPrice();
    }

    
    public boolean makePayment() {
        String userID = loggedInUser.getUserID();
        String transactionID = "TR" + String.format("%03d", cartManager.getTransactionIndex() + 1);

        ObservableList<Cart> cartItems = cartView.getCartItems();

        boolean success = cartManager.insertTransaction(userID, transactionID, cartItems);

        if (success) {
            cartManager.clearCart(userID);
            return true;
        } else {
            return false;
        }
    }


    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void navigateToHome() {
        HomeController homeController = new HomeController(loggedInUser);
        homeController.start(new Stage());
        cartView.close();
    }


    public double getTotalPrice() {
        return 0.0;
    }
    
    private void updateCartTotalPrice() {
        double totalPrice = calculateTotalPrice();
        cartView.setCartTotalPrice(totalPrice);
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void navigateToHistory(User loggedInUser) {
        HistoryController historyController = new HistoryController(new TransactionManager(), loggedInUser.getUserID(), loggedInUser);
        historyController.start();
        primaryStage.close(); 
    }

}
