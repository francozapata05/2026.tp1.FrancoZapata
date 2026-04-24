package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;
import java.util.List;

public interface PrestamoRepository extends Repository<Prestamo, Integer> {
    boolean estaDisponible(String isbn);
    long contarPrestamosSocio(String dni);
}
