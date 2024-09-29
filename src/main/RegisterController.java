package main;

import javafx.scene.control.Alert;
import javafx.scene.control.Toggle;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;

public class RegisterController {
    private Register registerView;
    private UserManager userManager;

    public RegisterController(Register registerView) {
        this.registerView = registerView;
        this.userManager = new UserManager(); 
    }

    public void start(Stage primaryStage) {
        registerView.initialize(primaryStage);
        registerView.getRegisterButton().setOnAction(e -> validateRegistration());
        primaryStage.show();
    }

    private void validateRegistration() {
        String username = registerView.getUsernameField().getText();
        String password = registerView.getPasswordField().getText();
        String confirmPassword = registerView.getConfirmPasswordField().getText();
        String phoneNumber = registerView.getPhoneNumberField().getText();
        String gender = null;
        Toggle selectedToggle = registerView.getGenderToggleGroup().getSelectedToggle();

        if (selectedToggle instanceof RadioButton) {
            gender = ((RadioButton) selectedToggle).getText();
        }

        String address = registerView.getAddressArea().getText();
        boolean agreeTerms = registerView.getAgreeTermsCheckBox().isSelected();

        if (username.isEmpty()) {
            showAlert("Error", "Username cannot be empty");
            return;
        }

        if (password.length() < 5) {
            showAlert("Error", "Password must contain at least 5 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        if (phoneNumber.length() != 12 || !phoneNumber.startsWith("+62")) {
            showAlert("Error", "Invalid phone number format. It must be +62XXXXXXXXXXXX");
            return;
        }

        if (gender == null) {
            showAlert("Error", "Please select your gender");
            return;
        }

        if (address.isEmpty()) {
            showAlert("Error", "Address cannot be empty");
            return;
        }

        if (!agreeTerms) {
            showAlert("Error", "Please agree to the terms and conditions");
            return;
        }

        String generatedUserId = generateUserId();

        boolean registrationSuccess = userManager.saveUser(generatedUserId, username, password, gender, phoneNumber, address, "User");

        if (registrationSuccess) {
            showRegistrationSuccessAlert();
            registerView.getStage().close();
            showLoginScene();
        } else {
            showAlert("Error", "Registration failed. Please try again.");
        }
    }
    
    private String generateUserId() {
        int userIndex = userManager.getUserCount() + 1;
        return "CU" + String.format("%03d", userIndex);
    }

    private void showLoginScene() {
        LoginController loginController = new LoginController(new UserManager()); 
        Stage loginStage = new Stage();
        loginController.start(loginStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showRegistrationSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Registration successful!");
        alert.showAndWait();
    }
}