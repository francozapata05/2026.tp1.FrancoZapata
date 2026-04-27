package com.bibliotech.service;

import com.bibliotech.exception.LibroNoEncontradoException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import com.bibliotech.repository.LibroRepository;
import java.util.List;

public class LibroServiceImpl implements LibroService {
    private final LibroRepository repositorio;

    public LibroServiceImpl(LibroRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) throws LibroNoEncontradoException {
        List<Recurso> resultado = repositorio.buscarTodos().stream()
                .filter(r -> r.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();
        if (resultado.isEmpty()) throw new LibroNoEncontradoException("título", titulo);
        return resultado;
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) throws LibroNoEncontradoException {
        List<Recurso> resultado = repositorio.buscarTodos().stream()
                .filter(r -> r.autor().toLowerCase().contains(autor.toLowerCase()))
                .toList();
        if (resultado.isEmpty()) throw new LibroNoEncontradoException("autor", autor);
        return resultado;
    }

    @Override
    public List<Recurso> buscarPorCategoria(Categoria categoria) throws LibroNoEncontradoException {
        List<Recurso> resultado = repositorio.buscarTodos().stream()
                .filter(r -> r.categoria() == categoria)
                .toList();
        if (resultado.isEmpty()) throw new LibroNoEncontradoException("categoría", categoria.name());
        return resultado;
    }
}