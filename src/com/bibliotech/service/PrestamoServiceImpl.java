package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Socio;
import com.bibliotech.model.TipoTransaccion;
import com.bibliotech.model.Transaccion;
import com.bibliotech.repository.PrestamoRepository;
import com.bibliotech.repository.TransaccionRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository repositorio;
    private final TransaccionRepository transaccionRepositorio;

    public PrestamoServiceImpl(PrestamoRepository repositorio, TransaccionRepository transaccionRepositorio) {
        this.repositorio = repositorio;
        this.transaccionRepositorio = transaccionRepositorio;
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
        transaccionRepositorio.guardar(new Transaccion(
                transaccionRepositorio.buscarPorSocio(socio.getId()).size() + 1,
                TipoTransaccion.PRESTAMO,
                prestamo,
                LocalDate.now()
        ));
        return prestamo;
    }

    @Override
    public long registrarDevolucion(int prestamoId) throws PrestamoNoEncontradoException {
        Prestamo prestamo = repositorio.buscarPorId(prestamoId)
                .orElseThrow(() -> new PrestamoNoEncontradoException(prestamoId));

        LocalDate fechaReal = LocalDate.now();
        long diasRetraso = Math.max(0, ChronoUnit.DAYS.between(prestamo.fechaDevolucionEsperada(), fechaReal));

        Prestamo devuelto = new Prestamo(
                prestamo.id(),
                prestamo.libro(),
                prestamo.socio(),
                prestamo.fechaPrestamo(),
                prestamo.fechaDevolucionEsperada(),
                Optional.of(fechaReal)
        );
        repositorio.actualizar(devuelto);
        transaccionRepositorio.guardar(new Transaccion(
                transaccionRepositorio.buscarPorSocio(prestamo.socio().getId()).size() + 1,
                TipoTransaccion.DEVOLUCION,
                devuelto,
                fechaReal
        ));
        return diasRetraso;
    }

    @Override
    public List<Transaccion> obtenerHistorial(int socioId) {
        return transaccionRepositorio.buscarPorSocio(socioId);
    }

    @Override
    public List<Prestamo> obtenerTodosLosPrestamos() {
        return repositorio.buscarTodos();
    }
}