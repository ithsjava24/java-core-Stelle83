package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRecord (UUID uuid, String name, Category category, BigDecimal price) {

    public ProductRecord {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (price == null) {
            price = BigDecimal.ZERO;  // Default to zero if null
        }
        if (uuid == null) {
            uuid = UUID.randomUUID();  // Generate a UUID if none provided
        }
    }
}