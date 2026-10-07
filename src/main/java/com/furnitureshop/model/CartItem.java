package com.furnitureshop.model;

import java.math.BigDecimal;

public record CartItem(Product product, int quantity) {

    public BigDecimal lineTotal() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}
