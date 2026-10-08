package com.furnitureshop.controller;

import com.furnitureshop.dao.SaleDAO;
import com.furnitureshop.exception.InsufficientStockException;
import com.furnitureshop.exception.ValidationException;
import com.furnitureshop.model.CartItem;
import com.furnitureshop.model.Customer;
import com.furnitureshop.model.Product;
import com.furnitureshop.model.User;
import com.furnitureshop.util.SessionManager;
import com.furnitureshop.util.Validator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SaleController {

    private final SaleDAO saleDAO = new SaleDAO();

    public void addToCart(List<CartItem> cart, Product product, int quantity)
            throws ValidationException, InsufficientStockException {
        if (product == null) {
            throw new ValidationException("Please select a product.");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be at least 1.");
        }
        for (int i = 0; i < cart.size(); i++) {
            CartItem existing = cart.get(i);
            if (existing.product().id() == product.id()) {
                int newQty = existing.quantity() + quantity;
                if (newQty > product.stockQty()) {
                    throw new InsufficientStockException(product.name(), product.stockQty());
                }
                cart.set(i, new CartItem(product, newQty));
                return;
            }
        }
        if (quantity > product.stockQty()) {
            throw new InsufficientStockException(product.name(), product.stockQty());
        }
        cart.add(new CartItem(product, quantity));
    }

    public BigDecimal subtotal(List<CartItem> cart) {
        return cart.stream().map(CartItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal parseDiscount(String text) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return Validator.decimal(text, "Discount", true);
    }

    /** Validates the bill and saves it. Returns the new bill number. */
    public int checkout(Customer customer, List<CartItem> cart, String discountText, String paymentMethod)
            throws ValidationException, InsufficientStockException, SQLException {
        if (customer == null) {
            throw new ValidationException("Please select a customer.");
        }
        if (cart.isEmpty()) {
            throw new ValidationException("The cart is empty. Add at least one item.");
        }
        BigDecimal subtotal = subtotal(cart);
        BigDecimal discount = parseDiscount(discountText);
        if (discount.compareTo(subtotal) > 0) {
            throw new ValidationException("Discount cannot be greater than the subtotal.");
        }
        BigDecimal total = subtotal.subtract(discount);
        User user = SessionManager.getInstance().getCurrentUser();
        return saleDAO.save(customer.id(), user.id(), discount, total, paymentMethod, cart);
    }
}
