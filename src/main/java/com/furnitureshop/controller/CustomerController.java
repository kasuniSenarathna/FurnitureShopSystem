package com.furnitureshop.controller;

import com.furnitureshop.dao.CustomerDAO;
import com.furnitureshop.exception.ValidationException;
import com.furnitureshop.model.Customer;
import com.furnitureshop.util.Validator;

import java.sql.SQLException;
import java.util.List;

public class CustomerController {

    private final CustomerDAO dao = new CustomerDAO();

    public List<Customer> search(String keyword) throws SQLException {
        return dao.findAll(keyword == null ? "" : keyword.trim());
    }

    public void add(String name, String phone, String email, String address)
            throws ValidationException, SQLException {
        validate(name, phone, email);
        dao.insert(new Customer(0, name.trim(), phone.trim(), blankToNull(email), blankToNull(address)));
    }

    public void update(int id, String name, String phone, String email, String address)
            throws ValidationException, SQLException {
        validate(name, phone, email);
        dao.update(new Customer(id, name.trim(), phone.trim(), blankToNull(email), blankToNull(address)));
    }

    public void delete(int id) throws SQLException {
        dao.delete(id);
    }

    private void validate(String name, String phone, String email) throws ValidationException {
        Validator.required(name, "Name");
        Validator.phone(phone);
        Validator.emailIfPresent(email);
    }

    private String blankToNull(String text) {
        return (text == null || text.trim().isEmpty()) ? null : text.trim();
    }
}
