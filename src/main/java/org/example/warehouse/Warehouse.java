package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final String name;
    private static final Map<String, Warehouse> instances = new HashMap<>();
    private final List<ProductRecord> products = new ArrayList<>();
    private final Set<ProductRecord> changedProducts = new HashSet<>();


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
                .ifPresentOrElse(changedProducts::add,
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