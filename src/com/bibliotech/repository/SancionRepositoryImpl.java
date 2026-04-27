package com.bibliotech.repository;

import com.bibliotech.model.Sancion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SancionRepositoryImpl implements SancionRepository {
    private final List<Sancion> sancionRepo = new ArrayList<>();

    @Override
    public void guardar(Sancion sancion) {
        sancionRepo.add(sancion);
    }

    @Override
    public List<Sancion> buscarPorSocio(int socioId) {
        return sancionRepo.stream()
                .filter(s -> s.socio().getId() == socioId)
                .toList();
    }

    @Override
    public Optional<Sancion> buscarSancionActiva(int socioId) {
        return sancionRepo.stream()
                .filter(s -> s.socio().getId() == socioId)
                .filter(s -> !s.fechaFin().isBefore(LocalDate.now()))
                .max(Comparator.comparing(Sancion::fechaFin));
    }
}