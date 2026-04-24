package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrestamoRepositoryImpl implements PrestamoRepository {
    private final Map<Integer, Prestamo> almacen = new HashMap<>();

    @Override
    public void guardar(Prestamo prestamo) {
        almacen.put(prestamo.id(), prestamo);
    }

    @Override
    public void actualizar(Prestamo prestamo) {
        almacen.put(prestamo.id(), prestamo);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return Optional.ofNullable(almacen.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public boolean estaDisponible(String isbn) {
        return almacen.values().stream()
                .filter(p -> p.fechaDevolucionReal().isEmpty())
                .noneMatch(p -> p.libro().isbn().equals(isbn));
    }

    @Override
    public long contarPrestamosSocio(String dni) {
        return almacen.values().stream()
                .filter(p -> p.fechaDevolucionReal().isEmpty())
                .filter(p -> p.socio().getDni().equals(dni))
                .count();
    }
}
