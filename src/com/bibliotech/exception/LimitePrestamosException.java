package com.bibliotech.exception;

public class LimitePrestamosException extends BibliotecaException {
    public LimitePrestamosException(String dni, int limite) {
        super("El socio con DNI " + dni + " alcanzó el límite de " + limite + " préstamos.");
    }
}