package com.furnitureshop.controller;

import com.furnitureshop.dao.CategoryDAO;
import com.furnitureshop.dao.ProductDAO;
import com.furnitureshop.exception.ValidationException;
import com.furnitureshop.model.Category;
import com.furnitureshop.model.Product;
import com.furnitureshop.util.Validator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Product> search(String keyword) throws SQLException {
        return productDAO.findAll(keyword == null ? "" : keyword.trim());
    }

    public List<Category> categories() throws SQLException {
        return categoryDAO.findAll();
    }

    public void add(Category category, String name, String material,
                    String price, String stock, String reorder) throws ValidationException, SQLException {
        productDAO.insert(build(0, category, name, material, price, stock, reorder));
    }

    public void update(int id, Category category, String name, String material,
                       String price, String stock, String reorder) throws ValidationException, SQLException {
        productDAO.update(build(id, category, name, material, price, stock, reorder));
    }

    public void delete(int id) throws SQLException {
        productDAO.delete(id);
    }

    private Product build(int id, Category category, String name, String material,
                          String price, String stock, String reorder) throws ValidationException {
        if (category == null) {
            throw new ValidationException("Please select a category.");
        }
        Validator.required(name, "Product name");
        BigDecimal priceValue = Validator.decimal(price, "Price", false);
        int stockValue = Validator.wholeNumber(stock, "Stock quantity");
        int reorderValue = Validator.wholeNumber(reorder, "Reorder level");
        String materialValue = (material == null || material.trim().isEmpty()) ? null : material.trim();
        return new Product(id, category.id(), category.name(), name.trim(), materialValue,
                priceValue, stockValue, reorderValue);
    }
}
