package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*;

public class Warehouse {


    private final List<ProductRecord> products = new ArrayList<>();
    private final Set<ProductRecord> changedProducts = new HashSet<>();


//    Factory method for Warehouse


    @SuppressWarnings("InfiniteRecursion")
    public static Warehouse getInstance(String defaultWarehouse) {
        return getInstance("Default Warehouse: " + defaultWarehouse);
    }


    //Add products to the warehouse
    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        ProductRecord product = new ProductRecord(id, name, category, price);

        //Check it product with the same name UUID already exist
        if (products.stream().anyMatch(p -> p.uuid().equals(product.uuid()))) {
            throw new IllegalArgumentException("Product already exists");
        }
        products.add(product);
        return product;
    }

    //Update the price of a product by its ID
    public void updateProductPrice(UUID id, BigDecimal newPrice) {

        ProductRecord product = getProductById(id).orElseThrow(() ->
                new IllegalArgumentException("Product with that id doesn't exist."));
        ProductRecord updatedProduct = new ProductRecord(product.uuid(), product.name(), product.category(), newPrice);
        products.remove(product);
        products.add(updatedProduct);
        changedProducts.add(updatedProduct);
    }

    // Get a product by its UUID
    public Optional<ProductRecord> getProductById(UUID id) {
        return products.stream().filter(p -> p.uuid().equals(id)).findFirst();
    }

    // Return all products
    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);
    }

    // Return products by category
    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream().filter(p -> p.category().equals(category)).toList();
    }

    // Return products grouped by category
    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return new HashMap<>();
    }

    // Check if the warehouse is empty
    public boolean isEmpty() {
        return products.isEmpty();
    }

    // Return changed products
    public List<ProductRecord> getChangedProducts() {
        return List.copyOf(changedProducts);
    }

}
