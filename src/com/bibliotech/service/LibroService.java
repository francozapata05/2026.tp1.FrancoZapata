package com.bibliotech.service;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Libro;
import java.util.List;

public interface LibroService {
    List<Libro> buscarPorTitulo(String titulo);
    List<Libro> buscarPorAutor(String autor);
    List<Libro> buscarPorCategoria(Categoria categoria);
}
