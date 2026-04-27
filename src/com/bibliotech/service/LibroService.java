package com.bibliotech.service;

import com.bibliotech.exception.LibroNoEncontradoException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import java.util.List;

public interface LibroService {
    List<Recurso> buscarPorTitulo(String titulo) throws LibroNoEncontradoException;
    List<Recurso> buscarPorAutor(String autor) throws LibroNoEncontradoException;
    List<Recurso> buscarPorCategoria(Categoria categoria) throws LibroNoEncontradoException;
}