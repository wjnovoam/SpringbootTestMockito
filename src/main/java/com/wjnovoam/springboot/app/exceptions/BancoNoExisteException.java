package com.wjnovoam.springboot.app.exceptions;

public class BancoNoExisteException extends RuntimeException {
    public BancoNoExisteException(String message) {
        super(message);
    }
}
