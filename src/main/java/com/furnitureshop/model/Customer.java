package com.furnitureshop.model;

public record Customer(int id, String name, String phone, String email, String address) {

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
