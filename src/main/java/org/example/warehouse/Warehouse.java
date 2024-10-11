//package org.example.warehouse;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//public class Warehouse {
//    private static final Map<String, Warehouse> instances = new HashMap<>();
//    private final String name;
//    private final List<ProductRecord> products = new ArrayList<>();
//    private final Set<ProductRecord> changedProducts = new HashSet<>();
//
//    // Private constructor to prevent direct instantiation
//    private Warehouse(String name) {
//        this.name = name;
//    }
//
//    // Factory method for Warehouse
//    public static Warehouse getInstance() {
//        return getInstance("Default Warehouse");
//    }
//
//    public static Warehouse getInstance(String name) {
//        return instances.computeIfAbsent(name, Warehouse::new);
//    }
//
//    // Add a product
//    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
//        ProductRecord product = new ProductRecord(id, name, category, price);
//
//        // Check if product with the same UUID already exists
//        if (products.stream().anyMatch(p -> p.uuid().equals(product.uuid()))) {
//            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
//        }
//        products.add(product);
//        return product;
//    }
//
//    // Update a product's price
//    public void updateProductPrice(UUID id, BigDecimal newPrice) {
//        ProductRecord product = getProductById(id).orElseThrow(() ->
//                new IllegalArgumentException("Product with that id doesn't exist."));
//        ProductRecord updatedProduct = new ProductRecord(product.uuid(), product.name(), product.category(), newPrice);
//        products.remove(product);
//        products.add(updatedProduct);
//        changedProducts.add(updatedProduct);
//    }
//
//    // Get a product by its UUID
//    public Optional<ProductRecord> getProductById(UUID id) {
//        return products.stream().filter(p -> p.uuid().equals(id)).findFirst();
//    }
//
//    // Return all products
//    public List<ProductRecord> getProducts() {
//        return Collections.unmodifiableList(products);
//    }
//
//    // Return products by category
//    public List<ProductRecord> getProductsBy(Category category) {
//        return products.stream().filter(p -> p.category().equals(category)).toList();
//    }
//
//    // Return products grouped by category
//    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
//        Map<Category, List<ProductRecord>> categoryMap = new HashMap<>();
//        for (ProductRecord product : products) {
//            categoryMap.computeIfAbsent(product.category(), k -> new ArrayList<>()).add(product);
//        }
//        return categoryMap;
////    }
//
//    // Check if the warehouse is empty
//    public boolean isEmpty() {
//        return products.isEmpty();
//    }
//
//    // Return changed products
//    public List<ProductRecord> getChangedProducts() {
//        return List.copyOf(changedProducts);
//    }
//}


package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

public class Warehouse {
    private static Warehouse instance;
    private final String name;
    private List<ProductRecord> products = new ArrayList<>();
    private final Set<UUID> changedProductIds = new HashSet<>();


    private Warehouse() {
        this.name = "MyStore";
    }

    public void clear() {
        products.clear();
    }


    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance() {
        if (instance == null) {
            instance = new Warehouse();

        } else instance.clearProducts();
        return instance;
    }

    private void clearProducts() {
        products.clear();
        changedProductIds.clear();
    }


    public static Warehouse getInstance(String name) {
        if (instance == null) {
            instance = new Warehouse(name);
        }
        return instance;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public static void resetInstance() {
        instance = null;
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (price == null) {
            price = BigDecimal.ZERO;
        }
        UUID finalId = id;
        if (products.stream().anyMatch(product -> product.uuid().equals(finalId))) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        ProductRecord product = new ProductRecord(id, name, category, price);
        products.add(product);
        return product;
    }

    public Optional<ProductRecord> getProductById(UUID id) {
        return products.stream()
                .filter(product -> product.uuid()
                        .equals(id))
                .findFirst();
    }

    public void updateProductPrice(UUID id, BigDecimal newPrice) {
        ProductRecord product = getProductById(id).orElseThrow(() ->
                new IllegalArgumentException("Product with that id doesn't exist."));

        product.setPrice(newPrice);
        changedProductIds.add(id);
    }

    public List<ProductRecord> getChangedProducts() {
        return products.stream()
                .filter(product -> changedProductIds.contains(product.uuid()))
                .collect(Collectors.toList());
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream()
                .filter(product -> product.category().equals(category))
                .collect(Collectors.toList());
    }

}