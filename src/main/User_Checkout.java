package main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class User_Checkout {

    public static boolean display(String confirmationText) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Payment Confirmation");

        Label confirmationLabel = new Label(confirmationText);
        confirmationLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button makePaymentButton = new Button("Make Payment");
        makePaymentButton.setStyle("-fx-background-color: #87CEEB; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-min-width: 100px; -fx-min-height: 40px; -fx-max-width: 200px; -fx-max-height: 60px;");
        boolean[] result = { false };

        makePaymentButton.setOnAction(e -> {
            result[0] = true;
            popupStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-min-width: 100px; -fx-min-height: 40px; -fx-max-width: 200px; -fx-max-height: 60px;");
        cancelButton.setOnAction(e -> popupStage.close());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 10px;");
        layout.getChildren().addAll(confirmationLabel, makePaymentButton, cancelButton);
        Scene scene = new Scene(layout, 500, 200);
        popupStage.setScene(scene);
        popupStage.showAndWait();

        return result[0]; 
    }
    
}
