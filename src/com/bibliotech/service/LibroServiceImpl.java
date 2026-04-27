package com.bibliotech.service;

import com.bibliotech.exception.LibroNoEncontradoException;
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
    public List<Libro> buscarPorTitulo(String titulo) throws LibroNoEncontradoException {
        List<Libro> resultado = repositorio.buscarTodos().stream()
                .filter(l -> l.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();
        if (resultado.isEmpty()) throw new LibroNoEncontradoException("título", titulo);
        return resultado;
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) throws LibroNoEncontradoException {
        List<Libro> resultado = repositorio.buscarTodos().stream()
                .filter(l -> l.autor().toLowerCase().contains(autor.toLowerCase()))
                .toList();
        if (resultado.isEmpty()) throw new LibroNoEncontradoException("autor", autor);
        return resultado;
    }

    @Override
    public List<Libro> buscarPorCategoria(Categoria categoria) throws LibroNoEncontradoException {
        List<Libro> resultado = repositorio.buscarTodos().stream()
                .filter(l -> l.categoria() == categoria)
                .toList();
        if (resultado.isEmpty()) throw new LibroNoEncontradoException("categoría", categoria.name());
        return resultado;
    }
}
