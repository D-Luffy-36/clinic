package com.an.DentalClinicSystem.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String itemName) {
        super(itemName);
    }
}
