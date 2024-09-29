package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class Register {
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField phoneNumberField;
    private ToggleGroup genderToggleGroup;
    private TextArea addressArea;
    private CheckBox agreeTermsCheckBox;
    private Button registerButton;
    private Stage stage;

    public void initialize(Stage primaryStage) {
        this.stage = primaryStage;

        primaryStage.setTitle("Register");

        BorderPane borderPane = new BorderPane();

        Label registerLabel = new Label("Register");
        registerLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 40));

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password:");
        Label phoneNumberLabel = new Label("Phone Number:");
        Label genderLabel = new Label("Gender:");
        Label addressLabel = new Label("Address:");

        usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");

        phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter phone number");

        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);

        addressArea = new TextArea();
        addressArea.setPromptText("Enter your address");

        agreeTermsCheckBox = new CheckBox("Agree to Terms & Conditions");

        registerButton = new Button("Register");
        registerButton.setMinWidth(200);
        registerButton.setId("registerButton");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(registerLabel, 0, 0, 2, 1);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 0, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 0, 4);
        grid.add(confirmPasswordLabel, 0, 5);
        grid.add(confirmPasswordField, 0, 6);
        grid.add(phoneNumberLabel, 0, 7);
        grid.add(phoneNumberField, 0, 8);
        grid.add(genderLabel, 0, 9);
        grid.add(new HBox(maleRadioButton, femaleRadioButton), 0, 10);
        grid.add(addressLabel, 0, 11);
        grid.add(addressArea, 0, 12);
        grid.add(agreeTermsCheckBox, 0, 13);
        grid.add(registerButton, 0, 14);

        GridPane.setMargin(registerLabel, new Insets(0, 0, 20, 0));

        borderPane.setCenter(grid);

        Label haveAccountLabel = new Label("Have an account? ");
        Hyperlink loginLink = new Hyperlink("Login here");
        loginLink.setOnAction(e -> handleLogin());

        HBox loginBox = new HBox(haveAccountLabel, loginLink);
        loginBox.setAlignment(Pos.CENTER);

        borderPane.setBottom(loginBox);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getPhoneNumberField() {
        return phoneNumberField;
    }

    public ToggleGroup getGenderToggleGroup() {
        return genderToggleGroup;
    }

    public TextArea getAddressArea() {
        return addressArea;
    }

    public CheckBox getAgreeTermsCheckBox() {
        return agreeTermsCheckBox;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Stage getStage() {
        return stage;
    }

    private void handleLogin() {
        stage.close();
        LoginController loginController = new LoginController(new UserManager());
        Stage loginStage = new Stage();
        loginController.start(loginStage);
    }
}
