package com.bibliotech.repository;

import com.bibliotech.model.Libro;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibroRepositoryImpl implements LibroRepository {
    private final Map<String, Libro> almacen = new HashMap<>();

    @Override
    public void guardar(Libro libro) {
        almacen.put(libro.isbn(), libro);
    }

    @Override
    public Optional<Libro> buscarPorId(String isbn) {
        return Optional.ofNullable(almacen.get(isbn));
    }

    @Override
    public List<Libro> buscarTodos() {
        return new ArrayList<>(almacen.values());
    }
}
