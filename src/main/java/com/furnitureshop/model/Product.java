package com.furnitureshop.model;

import java.math.BigDecimal;

public record Product(int id, int categoryId, String categoryName, String name, String material,
                      BigDecimal price, int stockQty, int reorderLevel) {

    @Override
    public String toString() {
        return name;
    }
}
