package com.furnitureshop.exception;

/** Thrown when a sale needs more units than are in stock. */
public class InsufficientStockException extends Exception {

    public InsufficientStockException(String productName, int available) {
        super("Not enough stock for '" + productName + "'. Available: " + available + ".");
    }

    public InsufficientStockException(String productName) {
        super("Not enough stock for '" + productName + "'. Stock has changed, please refresh and try again.");
    }
}
