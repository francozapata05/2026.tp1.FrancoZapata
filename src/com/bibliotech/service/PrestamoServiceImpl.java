package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.PrestamoRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
                LocalDate.now().plusDays(14),
                Optional.empty()
        );
        repositorio.guardar(prestamo);
        return prestamo;
    }

    @Override
    public long registrarDevolucion(int prestamoId) throws PrestamoNoEncontradoException {
        Prestamo prestamo = repositorio.buscarPorId(prestamoId)
                .orElseThrow(() -> new PrestamoNoEncontradoException(prestamoId));

        LocalDate fechaReal = LocalDate.now();
        long diasRetraso = Math.max(0, ChronoUnit.DAYS.between(prestamo.fechaDevolucionEsperada(), fechaReal));

        repositorio.actualizar(new Prestamo(
                prestamo.id(),
                prestamo.libro(),
                prestamo.socio(),
                prestamo.fechaPrestamo(),
                prestamo.fechaDevolucionEsperada(),
                Optional.of(fechaReal)
        ));

        return diasRetraso;
    }
}