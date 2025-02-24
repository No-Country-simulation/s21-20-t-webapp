package com.inventario.demo.config.exceptions;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
