package com.furnitureshop.controller;

import com.furnitureshop.dao.UserDAO;
import com.furnitureshop.exception.ValidationException;
import com.furnitureshop.model.User;
import com.furnitureshop.util.PasswordUtil;
import com.furnitureshop.util.Validator;

import java.sql.SQLException;

public class AuthController {

    private final UserDAO dao = new UserDAO();

    public User login(String username, String password) throws ValidationException, SQLException {
        Validator.required(username, "Username");
        Validator.required(password, "Password");
        User user = dao.authenticate(username.trim(), PasswordUtil.sha256(password));
        if (user == null) {
            throw new ValidationException("Invalid username or password.");
        }
        return user;
    }
}
