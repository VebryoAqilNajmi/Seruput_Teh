package main;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main1 extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            UserManager userManager = new UserManager();
            LoginController loginController = new LoginController(userManager);
            loginController.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}