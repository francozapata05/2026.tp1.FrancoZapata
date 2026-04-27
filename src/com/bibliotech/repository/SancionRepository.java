package com.bibliotech.repository;

import com.bibliotech.model.Sancion;
import java.util.List;
import java.util.Optional;

public interface SancionRepository {
    void guardar(Sancion sancion);
    List<Sancion> buscarPorSocio(int socioId);
    Optional<Sancion> buscarSancionActiva(int socioId);
}