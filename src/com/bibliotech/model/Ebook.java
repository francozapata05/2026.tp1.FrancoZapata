package com.bibliotech.model;

public record Ebook(String isbn, String titulo, String autor, int paginas, Categoria categoria, FormatoEbook formato) implements Recurso {
}
