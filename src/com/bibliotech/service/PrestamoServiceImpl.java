package com.bibliotech.service;

import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.exception.SocioSancionadoException;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Sancion;
import com.bibliotech.model.Socio;
import com.bibliotech.model.TipoTransaccion;
import com.bibliotech.model.Transaccion;
import com.bibliotech.repository.PrestamoRepository;
import com.bibliotech.repository.SancionRepository;
import com.bibliotech.repository.TransaccionRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository repositorio;
    private final TransaccionRepository transaccionRepositorio;
    private final SancionRepository sancionRepositorio;

    public PrestamoServiceImpl(PrestamoRepository repositorio, TransaccionRepository transaccionRepositorio, SancionRepository sancionRepositorio) {
        this.repositorio = repositorio;
        this.transaccionRepositorio = transaccionRepositorio;
        this.sancionRepositorio = sancionRepositorio;
    }

    @Override
    public Prestamo realizarPrestamo(Recurso recurso, Socio socio) throws LibroNoDisponibleException, LimitePrestamosException, SocioSancionadoException {
        Optional<Sancion> sancionActiva = sancionRepositorio.buscarSancionActiva(socio.getId());
        if (sancionActiva.isPresent()) {
            throw new SocioSancionadoException(socio.getDni(), sancionActiva.get().fechaFin());
        }
        if (!repositorio.estaDisponible(recurso.isbn())) {
            throw new LibroNoDisponibleException(recurso.isbn());
        }
        if (repositorio.contarPrestamosSocio(socio.getDni()) >= socio.getLimitePrestamos()) {
            throw new LimitePrestamosException(socio.getDni(), socio.getLimitePrestamos());
        }
        Prestamo prestamo = new Prestamo(
                generarId(),
                recurso,
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

    private static long generarId() {
        return System.currentTimeMillis() * 10000 + new Random().nextInt(10000);
    }

    @Override
    public long registrarDevolucion(long prestamoId) throws PrestamoNoEncontradoException {
        Prestamo prestamo = repositorio.buscarPorId(prestamoId)
                .orElseThrow(() -> new PrestamoNoEncontradoException(prestamoId));

        LocalDate fechaReal = LocalDate.now();
        long diasRetraso = Math.max(0, ChronoUnit.DAYS.between(prestamo.fechaDevolucionEsperada(), fechaReal));

        Prestamo devuelto = new Prestamo(
                prestamo.id(),
                prestamo.recurso(),
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

        if (diasRetraso > 0) {
            List<Sancion> sancionesPrevias = sancionRepositorio.buscarPorSocio(prestamo.socio().getId());
            int duracion = sancionesPrevias.size() >= 3 ? 30 : (int) diasRetraso;
            sancionRepositorio.guardar(new Sancion(
                    sancionesPrevias.size() + 1,
                    prestamo.socio(),
                    fechaReal,
                    fechaReal.plusDays(duracion)
            ));
        }

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

    @Override
    public List<Sancion> obtenerSanciones(int socioId) {
        return sancionRepositorio.buscarPorSocio(socioId);
    }
}