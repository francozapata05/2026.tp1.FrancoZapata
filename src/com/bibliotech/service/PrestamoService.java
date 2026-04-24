package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Socio;
import com.bibliotech.model.Transaccion;
import java.util.List;

public interface PrestamoService {
    Prestamo realizarPrestamo(Libro libro, Socio socio) throws LibroNoDisponibleException, LimitePrestamosException;
    long registrarDevolucion(int prestamoId) throws PrestamoNoEncontradoException;
    List<Transaccion> obtenerHistorial(int socioId);
    List<Prestamo> obtenerTodosLosPrestamos();
}
