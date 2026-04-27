package com.bibliotech.exception;

public class LibroNoEncontradoException extends BibliotecaException {
    public LibroNoEncontradoException(String criterio, String valor) {
        super("No se encontraron libros con " + criterio + ": " + valor);
    }
}