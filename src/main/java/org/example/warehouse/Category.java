package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Category {
    private final String name;
    private static final Map<String, Category> categoryCache = new HashMap<>();

    // Private constructor to prevent public instantiation
    private Category(String name) {
        // Capitalize first letter
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    // Get or create a Category
    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }

        // Check the cache and return the same instance for identical names
        return categoryCache.computeIfAbsent(name, Category::new);
    }

    // Getter for the name
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
