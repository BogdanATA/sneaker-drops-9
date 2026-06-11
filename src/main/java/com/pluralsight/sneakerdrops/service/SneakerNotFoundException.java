package com.pluralsight.sneakerdrops.service;

public class SneakerNotFoundException extends RuntimeException {
    public SneakerNotFoundException(String message) {
        super(message);
    }
}
