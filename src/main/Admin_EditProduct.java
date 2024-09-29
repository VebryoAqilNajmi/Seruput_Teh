package main;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class Admin_EditProduct {
    private TableView<Product> productTable;
    private TextField nameTextField;
    private TextField priceTextField;
    private TextArea descriptionTextArea;
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;

    @SuppressWarnings({ "deprecation" })
    public void initialize(Stage primaryStage, EditProductController controller) {
        primaryStage.setTitle("SeRuput Teh");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));

        HBox mainHBox = new HBox();
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setSpacing(10);

        VBox leftVBox = new VBox();
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setSpacing(10);

        productTable = new TableView<>();
        productTable.setPrefWidth(400); 
        productTable.setPrefHeight(500); 
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        productTable.getColumns().add(nameColumn);

        productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showProductDetails(newValue);
            } else {
                clearProductDetails();
            }
        });

        productTable.widthProperty().addListener((obs, oldVal, newVal) -> {
            TableHeaderRow header = (TableHeaderRow) productTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal1, newVal1) -> header.setReordering(false));
        });

        Label editProductLabel = new Label("Manage Product");
        editProductLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 40));

        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().add(editProductLabel);

        leftVBox.getChildren().addAll(labelBox, productTable);

        VBox rightVBox = new VBox();
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setSpacing(10);

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        nameTextField = new TextField();
        nameTextField.setPrefWidth(200);

        descriptionTextArea = new TextArea();
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setPrefWidth(300);
        descriptionTextArea.setPrefHeight(200);

        priceTextField = new TextField();
        priceTextField.setPrefWidth(200);

        addButton = new Button("Add");
        addButton.setPrefWidth(200);
        updateButton = new Button("Update");
        updateButton.setPrefWidth(200);
        deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(200);

        inputGrid.add(new Label("Product Title:"), 0, 0);
        inputGrid.add(nameTextField, 0, 1);
        inputGrid.add(new Label("Product Description:"), 0, 2);
        inputGrid.add(descriptionTextArea, 0, 3);
        inputGrid.add(new Label("Price:"), 0, 4);
        inputGrid.add(priceTextField, 0, 5);
        inputGrid.add(addButton, 0, 6);
        inputGrid.add(updateButton, 0, 7);
        inputGrid.add(deleteButton, 0, 8);

        rightVBox.getChildren().addAll(inputGrid);

        mainHBox.getChildren().addAll(leftVBox, rightVBox);

        borderPane.setTop(createMenuBar(primaryStage, controller));
        borderPane.setCenter(mainHBox);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
    }

    private MenuBar createMenuBar(Stage primaryStage, EditProductController controller) {
        MenuBar menuBar = new MenuBar();
        MenuItem manageProductItem = new MenuItem("Manage Product");

        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            Stage currentStage = (Stage) productTable.getScene().getWindow();
            currentStage.close();
            Login loginView = new Login();
            LoginController loginController = new LoginController(loginView);
            loginController.start(new Stage());
        });

        Menu manageProductMenu = new Menu("Manage Product");
        manageProductMenu.getItems().add(manageProductItem);

        Menu logoutMenu = new Menu("Logout");
        logoutMenu.getItems().add(logoutItem);

        menuBar.getMenus().addAll( manageProductMenu, logoutMenu);

        return menuBar;
    }


    private void showProductDetails(Product selectedProduct) {
        nameTextField.setText(selectedProduct.getProductName());
        priceTextField.setText(String.valueOf(selectedProduct.getProductPrice()));
        descriptionTextArea.setText(selectedProduct.getProductDescription());
    }

    private void clearProductDetails() {
        nameTextField.clear();
        priceTextField.clear();
        descriptionTextArea.clear();
    }

    public void setProductData(ObservableList<Product> data) {
        productTable.setItems(data);
    }

    public String getNameInput() {
        return nameTextField.getText();
    }

    public long getPriceInput() {
        try {
            return Long.parseLong(priceTextField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void clearInsertFields() {
        nameTextField.clear();
        priceTextField.clear();
        descriptionTextArea.clear();
    }

    public String getDescriptionInput() {
        return descriptionTextArea.getText();
    }

    public Product getSelectedProduct() {
        return productTable.getSelectionModel().getSelectedItem();
    }

    public void setAddButtonHandler(Runnable handler) {
        addButton.setOnAction(e -> handler.run());
    }

    public void setUpdateButtonHandler(Runnable handler) {
        updateButton.setOnAction(e -> {
            handler.run();
            clearProductDetails();
        });
    }

    public void setDeleteButtonHandler(Runnable handler) {
        deleteButton.setOnAction(e -> handler.run());
    }
}
