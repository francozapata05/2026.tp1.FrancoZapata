package com.bibliotech.service;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Libro;
import com.bibliotech.repository.LibroRepository;
import java.util.List;

public class LibroServiceImpl implements LibroService {
    private final LibroRepository repositorio;

    public LibroServiceImpl(LibroRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Libro> buscarPorTitulo(String titulo) {
        return repositorio.buscarTodos().stream()
                .filter(l -> l.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) {
        return repositorio.buscarTodos().stream()
                .filter(l -> l.autor().toLowerCase().contains(autor.toLowerCase()))
                .toList();
    }

    @Override
    public List<Libro> buscarPorCategoria(Categoria categoria) {
        return repositorio.buscarTodos().stream()
                .filter(l -> l.categoria() == categoria)
                .toList();
    }
}
