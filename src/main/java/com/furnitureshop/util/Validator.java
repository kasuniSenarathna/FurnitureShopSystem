package com.furnitureshop.util;

import com.furnitureshop.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Validator {

    private Validator() {
    }

    public static void required(String value, String field) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(field + " is required.");
        }
    }

    public static void phone(String value) throws ValidationException {
        required(value, "Phone");
        if (!value.trim().matches("0\\d{9}")) {
            throw new ValidationException("Phone must be 10 digits and start with 0 (e.g. 0771234567).");
        }
    }

    public static void emailIfPresent(String value) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            return;
        }
        if (!value.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new ValidationException("Email address is not valid.");
        }
    }

    public static BigDecimal decimal(String value, String field, boolean allowZero) throws ValidationException {
        required(value, field);
        BigDecimal number;
        try {
            number = new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(field + " must be a valid number.");
        }
        int cmp = number.compareTo(BigDecimal.ZERO);
        if (cmp < 0) {
            throw new ValidationException(field + " cannot be negative.");
        }
        if (cmp == 0 && !allowZero) {
            throw new ValidationException(field + " must be greater than 0.");
        }
        return number.setScale(2, RoundingMode.HALF_UP);
    }

    public static int wholeNumber(String value, String field) throws ValidationException {
        required(value, field);
        int number;
        try {
            number = Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(field + " must be a whole number.");
        }
        if (number < 0) {
            throw new ValidationException(field + " cannot be negative.");
        }
        return number;
    }
}
