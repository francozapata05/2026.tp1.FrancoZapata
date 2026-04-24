package com.bibliotech.exception;

public class PrestamoNoEncontradoException extends BibliotecaException {
    public PrestamoNoEncontradoException(int id) {
        super("No se encontró un préstamo con ID: " + id);
    }
}