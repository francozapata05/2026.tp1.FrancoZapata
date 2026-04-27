package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrestamoRepositoryImpl implements PrestamoRepository {
    private final Map<Long, Prestamo> prestamoRepo = new HashMap<>();

    @Override
    public void guardar(Prestamo prestamo) {
        prestamoRepo.put(prestamo.id(), prestamo);
    }

    @Override
    public void actualizar(Prestamo prestamo) {
        prestamoRepo.put(prestamo.id(), prestamo);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Long id) {
        return Optional.ofNullable(prestamoRepo.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(prestamoRepo.values());
    }

    @Override
    public boolean estaDisponible(String isbn) {
        return prestamoRepo.values().stream()
                .filter(p -> p.fechaDevolucionReal().isEmpty())
                .noneMatch(p -> p.libro().isbn().equals(isbn));
    }

    @Override
    public long contarPrestamosSocio(String dni) {
        return prestamoRepo.values().stream()
                .filter(p -> p.fechaDevolucionReal().isEmpty())
                .filter(p -> p.socio().getDni().equals(dni))
                .count();
    }
}
