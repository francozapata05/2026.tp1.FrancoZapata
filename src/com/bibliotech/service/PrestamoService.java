package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.exception.SocioSancionadoException;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Sancion;
import com.bibliotech.model.Socio;
import com.bibliotech.model.Transaccion;
import java.util.List;

public interface PrestamoService {
    Prestamo realizarPrestamo(Recurso recurso, Socio socio) throws LibroNoDisponibleException, LimitePrestamosException, SocioSancionadoException;
    long registrarDevolucion(long prestamoId) throws PrestamoNoEncontradoException;
    List<Transaccion> obtenerHistorial(int socioId);
    List<Prestamo> obtenerTodosLosPrestamos();
    List<Sancion> obtenerSanciones(int socioId);
}
