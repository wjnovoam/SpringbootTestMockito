package com.wjnovoam.springboot.app.exceptions;

public class CuentaNoExisteException extends RuntimeException{
    public CuentaNoExisteException(String message) {
        super(message);
    }
}
