package com.bibliotech.model;

public record Libro(String isbn, String titulo, String autor, int anio, Categoria categoria) implements Recurso {
}
