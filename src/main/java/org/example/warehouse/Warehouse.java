package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*;

public class Warehouse {

//    private static final Map<String, Warehouse> instances = new HashMap<>();
    private final String name;
    private final List<ProductRecord> products = new ArrayList<>();
    private Set<ProductRecord> changedProducts = new HashSet<>();

//    Private constructor to prevent direct instantiation
    private Warehouse(String name) {
        this.name = name;
    }


//    Factory method for Warehouse ___> ma tegin teisiti kui chat ytles

//    public static Warehouse getInstance() {
//        return getInstance("Default Warehouse");
//    }

    public static Warehouse getInstance(String defaultWarehouse) {
        return getInstance("Deaault Warehouse: " + defaultWarehouse);
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
        Map<Category, List<ProductRecord>> categoryMap = new HashMap<>();
        for (ProductRecord product : products) {
            categoryMap.computeIfAbsent(product.category(), k -> new ArrayList<>()).add(product);
        }
        return categoryMap;
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
