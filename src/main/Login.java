package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class Login {
    private TextField usernameField;
    private PasswordField passwordField;
    private Scene scene;

    public void initialize(Stage primaryStage, LoginController loginController) {
        primaryStage.setTitle("Login");

        BorderPane borderPane = new BorderPane();

        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 40));

        Label usernameLabel = new Label("Username:");

        Label passwordLabel = new Label("Password:");

        usernameField = new TextField();
        usernameField.setPromptText("username");

        passwordField = new PasswordField();
        passwordField.setPromptText("password");

        Label haveAccountLabel = new Label("Don't have an account yet? ");
        Hyperlink registerLink = new Hyperlink("Register Here");
        registerLink.setTextFill(Color.BLUE);
        registerLink.setOnAction(e -> handleRegister());

        Button loginButton = new Button("Login");
        loginButton.setMinWidth(200);
        loginButton.setId("loginButton");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(haveAccountLabel, 1, 5, 1,1);
        grid.add(loginLabel, 0, 0, 2, 2);
        grid.add(registerLink, 1, 6, 2, 1);
        grid.add(usernameLabel, 0, 2);
        grid.add(passwordLabel, 0, 3);
        grid.add(usernameField, 1, 2);
        grid.add(passwordField, 1, 3);
        grid.add(loginButton, 1, 4);

        GridPane.setMargin(loginLabel, new Insets(0, 0, 20, 100));

        borderPane.setCenter(grid);

        scene = new Scene(borderPane, 400, 300);
        primaryStage.setScene(scene);
    }


    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Scene getScene() {
        return scene;
    }

    public void setLoginButtonHandler(Runnable handler) {
        Button loginButton = (Button) scene.lookup("#loginButton");
        loginButton.setOnAction(e -> handler.run());
    }

    private void handleRegister() {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();
        currentStage.close();
        Register registerView = new Register();
        RegisterController registerController = new RegisterController(registerView);
        registerController.start(new Stage());
    }

    public void close() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
