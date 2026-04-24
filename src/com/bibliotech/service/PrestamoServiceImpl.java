package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.PrestamoRepository;
import java.time.LocalDate;

public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository repositorio;

    public PrestamoServiceImpl(PrestamoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Prestamo realizarPrestamo(Libro libro, Socio socio) throws LibroNoDisponibleException, LimitePrestamosException {
        if (!repositorio.estaDisponible(libro.isbn())) {
            throw new LibroNoDisponibleException(libro.isbn());
        }
        if (repositorio.contarPrestamosSocio(socio.getDni()) >= socio.getLimitePrestamos()) {
            throw new LimitePrestamosException(socio.getDni(), socio.getLimitePrestamos());
        }
        Prestamo prestamo = new Prestamo(
                repositorio.buscarTodos().size() + 1,
                libro,
                socio,
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        );
        repositorio.guardar(prestamo);
        return prestamo;
    }
}
