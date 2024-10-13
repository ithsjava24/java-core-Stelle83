package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final String name;
    private static Map<String, Warehouse> instances = new HashMap<>();
    private List<ProductRecord> products = new ArrayList<>();
    private Set<ProductRecord> changedProducts = new HashSet<>();


    private Warehouse(String store) {
        this.name = store;
    }

    public static Warehouse getInstance(String myStore) {
        return instances.computeIfAbsent(myStore, Warehouse::new);
    }

    public static Warehouse getInstance() {
        return new Warehouse(" ");
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        return products.stream()
                .filter(product -> product.uuid().equals(uuid))
                .findFirst();
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getProductsBy(Category category) {
        List<ProductRecord> sameCategory = new ArrayList<>();
        for (ProductRecord productUUID : products) {
            if (category == productUUID.category()) {
                sameCategory.add(productUUID);
            }
        }
        return sameCategory;
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        products.stream()
                .filter(product -> product.uuid().equals(uuid))
                .findAny()
                .ifPresentOrElse(product -> changedProducts.add(product),
                        () -> {
                            throw new IllegalArgumentException("Product with that id doesn't exist.");
                        });
        products.replaceAll(product -> product.uuid().equals(uuid) ? new ProductRecord(product.uuid(), product.name(), product.category(), price) :
                product);
    }

    public Set<ProductRecord> getChangedProducts() {
        return changedProducts;
    }

    public ProductRecord addProduct(UUID uuid, String milk, Category dairy, BigDecimal bigDecimal) {
        ProductRecord product = new ProductRecord(uuid, milk, dairy, bigDecimal);
        for (ProductRecord eachProduct : products) {
            if (uuid == eachProduct.uuid()) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }
        }
        products.add(product);
        return product;
    }
}

//package org.example.warehouse;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//public class Warehouse {
//    private static final Map<String, Warehouse> instances = new HashMap<>();
//    private final String name;
//    private List<ProductRecord> products = new ArrayList<>();
//    private Set<ProductRecord> changedProducts = new HashSet<>();
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
//    public void ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
//        ProductRecord product = new ProductRecord(id, name, category, price);
//        // Check if product with the same UUID already exists
//        if (products.stream().anyMatch(p -> p.uuid().equals(product.uuid()))) {
//            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
//        }
//        products.add(product);
//        return product;
//    }
//
//    // Update a product's price
//    public boolean updateProductPrice(UUID id, BigDecimal newPrice) {
//        if (!products.contains(id)) {
//            throw new IllegalArgumentException("Product with that id does not exist, use updateProduct for updates.");
//        }
//        //Ensure price is non-null; default to BigDecimal.ZERO if null
//        newPrice = (newPrice != null) ? newPrice : BigDecimal.ZERO;
//
//        // Create a new ProductRecord with the updated price
//        ProductRecord existingProduct = products.get(id);
//        ProductRecord updatedProduct = new ProductRecord(
//                existingProduct.uuid(),
//                existingProduct.name(),
//                existingProduct.category(),
//                newPrice
//        );
//
//        // Update the product in the map
//        products.put(id, updatedProduct);
//
//        // Mark this product as changed
//        changedProducts.add(updatedProduct);
//
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
//    }
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
//
//
//
////
////import java.math.BigDecimal;
////import java.util.*;
////import java.util.stream.Collectors;
////
////public class Warehouse {
////    private static final Map<String, Warehouse> instances = new HashMap<>();
//////    private static Warehouse instances;
////    private final String name;
////    private final List<ProductRecord> products = new ArrayList<>();
////    private final Set<UUID> changedProducts = new HashSet<>();
////
////
////
//////  /*  PUUDU SIIT MIDAGI
////    private Warehouse() {
////        this.name = "MyStore";
////    }
////
////    public void clear() {
////        products.clear();
////    }
//////*/
////
////
////    //    // Private constructor to prevent direct instantiation
////
////    private Warehouse(String name) {
////        this.name = name;
////    }
////
////    // Factory method for Warehouse
////    public static Warehouse getInstance() {
////        return getInstance("Default Warehouse");
////    }
////
////    public static Warehouse getInstance(String name) {
////        return instances.computeIfAbsent(name, Warehouse::new);
////    }
////
////    // Add a product
////    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
////        ProductRecord product = new ProductRecord(id, name, category, price);
////
////        // Check if product with the same UUID already exists
////        if (products.stream().anyMatch(product -> product.uuid().equals(product.uuid()))) {
////            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
////        }
////        products.add(product);
////        return product;
////    }
////
////    // Update a product's price
////    public void updateProductPrice(UUID id, BigDecimal newPrice) {
////        ProductRecord product = getProductById(id).orElseThrow(() ->
////                new IllegalArgumentException("Product with that id doesn't exist."));
////        ProductRecord updatedProduct = new ProductRecord(product.uuid(), product.name(), product.category(), newPrice);
////        products.remove(product);
////        products.add(updatedProduct);
////        changedProducts.add(updatedProduct.uuid());
////    }
////
////    // Get a product by its UUID
////    public Optional<ProductRecord> getProductById(UUID id) {
////        return products.stream().filter(p -> p.uuid().equals(id)).findFirst();
////    }
////
////    // Return all products
////    public List<ProductRecord> getProducts() {
////        return Collections.unmodifiableList(products);
////    }
////
////    // Return products by category
////    public List<ProductRecord> getProductsBy(Category category) {
////        return products.stream().filter(p -> p.category().equals(category)).toList();
////    }
////
////    // Return products grouped by category
////    public Collection<UUID> getProductsGroupedByCategories() {
////        Map<Category, List<ProductRecord>> categoryMap = new HashMap<>();
////        for (ProductRecord product : products) {
////            categoryMap.computeIfAbsent(product.category(), k -> new ArrayList<>()).add(product);
////        }
////        return categoryMap.isEmpty();
//////    }
////
////    // Check if the warehouse is empty
////    public boolean isEmpty() {
////        return products.isEmpty();
////    }
////
////    // Return changed products
////    public List<ProductRecord> getChangedProducts() {
////        return List.copyOf(changedProducts);
////    }
////}
////
//////
//////package org.example.warehouse;
//////
//////import java.math.BigDecimal;
//////import java.util.*;
//////import java.util.stream.Collectors;
//////import java.util.List;
//////
//////public class Warehouse {
//////    private static Warehouse instance;
//////    private final String name;
//////    private List<ProductRecord> products = new ArrayList<>();
//////    private final Set<UUID> changedProductIds = new HashSet<>();
//////
//////
//////    private Warehouse() {
//////        this.name = "MyStore";
//////    }
//////
//////    public void clear() {
//////        products.clear();
//////    }
//////
//////
//////    private Warehouse(String name) {
//////        this.name = name;
//////    }
//////
//////    public static Warehouse getInstance() {
//////        if (instance == null) {
//////            instance = new Warehouse();
//////
//////        } else instance.clearProducts();
//////        return instance;
//////    }
//////
//////    private void clearProducts() {
//////        products.clear();
//////        changedProductIds.clear();
//////    }
//////
//////
//////    public static Warehouse getInstance(String name) {
//////        if (instance == null) {
//////            instance = new Warehouse(name);
//////        }
//////        return instance;
//////    }
//////
//////    public boolean isEmpty() {
//////        return products.isEmpty();
//////    }
//////
//////    public static void resetInstance() {
//////        instance = null;
//////    }
//////
//////    public List<ProductRecord> getProducts() {
//////        return Collections.unmodifiableList(products);
//////    }
//////
//////    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
//////        if (name == null || name.isEmpty()) {
//////            throw new IllegalArgumentException("Product name can't be null or empty.");
//////        }
//////        if (category == null) {
//////            throw new IllegalArgumentException("Category can't be null.");
//////        }
//////        if (id == null) {
//////            id = UUID.randomUUID();
//////        }
//////        if (price == null) {
//////            price = BigDecimal.ZERO;
//////        }
//////        UUID finalId = id;
//////        if (products.stream().anyMatch(product -> product.uuid().equals(finalId))) {
//////            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
//////        }
//////
//////        ProductRecord product = new ProductRecord(id, name, category, price);
//////        products.add(product);
//////        return product;
//////    }
//////
//////    public Optional<ProductRecord> getProductById(UUID id) {
//////        return products.stream()
//////                .filter(product -> product.uuid()
//////                        .equals(id))
//////                .findFirst();
//////    }
//////
//////    public void updateProductPrice(UUID id, BigDecimal newPrice) {
//////        ProductRecord product = getProductById(id).orElseThrow(() ->
//////                new IllegalArgumentException("Product with that id doesn't exist."));
//////
//////        product.setPrice(newPrice);
//////        changedProductIds.add(id);
//////    }
//////
//////    public List<ProductRecord> getChangedProducts() {
//////        return products.stream()
//////                .filter(product -> changedProductIds.contains(product.uuid()))
//////                .collect(Collectors.toList());
//////    }
//////
//////    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
//////        return products.stream()
//////                .collect(Collectors.groupingBy(ProductRecord::category));
//////    }
//////
//////    public List<ProductRecord> getProductsBy(Category category) {
//////        return products.stream()
//////                .filter(product -> product.category().equals(category))
//////                .collect(Collectors.toList());
//////    }
//////
////}