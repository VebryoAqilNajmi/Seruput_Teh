package main;

import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.util.List;

public class EditProductController {
    private Admin_EditProduct editProductView;
    private ProductManager productManager;
    public EditProductController(User loggedInUser) {
    	this.productManager = new ProductManager();
        editProductView = new Admin_EditProduct();
        editProductView.initialize(new Stage(), this);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("SNeam");

        editProductView = new Admin_EditProduct();
        editProductView.initialize(primaryStage, this);
        setButtonHandlers();

        updateProductTable();

        primaryStage.show();
    }

    private void setButtonHandlers() {
        editProductView.setUpdateButtonHandler(() -> handleUpdate());
        editProductView.setDeleteButtonHandler(() -> handleDelete());
        editProductView.setAddButtonHandler(() -> handleAdd()); 
    }

    public void handleAdd() { 
        String name = editProductView.getNameInput(); 
        Long price = editProductView.getPriceInput(); 
        String description = editProductView.getDescriptionInput(); 

        Product newProduct = new Product();
        newProduct.setProductName(name);
        newProduct.setProductPrice(price);
        newProduct.setProductDescription(description);

        boolean success = addProductToDatabase(newProduct);

        if (success) {
            updateProductTable();
            editProductView.clearInsertFields(); 
        } else {
            System.out.println("Failed to add product to the database.");
        }
    }

    private boolean addProductToDatabase(Product product) {
        return productManager.addProductToDatabase(product);
    }

    private void updateProductTable() {
        List<Product> allProducts = productManager.getAllProducts();
        editProductView.setProductData(FXCollections.observableArrayList(allProducts));
    }

    public void handleUpdate() {
        Product selectedProduct = editProductView.getSelectedProduct();
        long newPrice = editProductView.getPriceInput();
        String newDescription = editProductView.getDescriptionInput();
        String newName = editProductView.getNameInput();

        selectedProduct.setProductName(newName);
        selectedProduct.setProductDescription(newDescription);

        boolean success = updateProduct(selectedProduct, newPrice, newName, newDescription);

        if (success) {
            updateProductTable();
        } else {
            System.out.println("Failed to update product.");
        }
    }
    
    public boolean updateProduct(Product product, long newPrice,String newName, String newDescription) { 
        return productManager.updateProduct(product, newPrice, newName, newDescription);
    }

    public void handleDelete() {
        Product selectedProduct = editProductView.getSelectedProduct();

        boolean success = deleteProduct(selectedProduct);

        if (success) {
            updateProductTable();
            editProductView.clearInsertFields();
        } else {
            System.out.println("Failed to delete product.");
        }
    }

    private boolean deleteProduct(Product product) {
        return productManager.deleteProduct(product);
    }
}
