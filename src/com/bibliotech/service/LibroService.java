package com.bibliotech.service;

import com.bibliotech.exception.LibroNoEncontradoException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Libro;
import java.util.List;

public interface LibroService {
    List<Libro> buscarPorTitulo(String titulo) throws LibroNoEncontradoException;
    List<Libro> buscarPorAutor(String autor) throws LibroNoEncontradoException;
    List<Libro> buscarPorCategoria(Categoria categoria) throws LibroNoEncontradoException;
}
