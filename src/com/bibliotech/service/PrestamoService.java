package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Socio;

public interface PrestamoService {
    Prestamo realizarPrestamo(Libro libro, Socio socio) throws LibroNoDisponibleException, LimitePrestamosException;
}
