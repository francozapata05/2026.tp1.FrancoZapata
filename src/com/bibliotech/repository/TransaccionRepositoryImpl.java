package com.bibliotech.repository;

import com.bibliotech.model.Transaccion;
import java.util.ArrayList;
import java.util.List;

public class TransaccionRepositoryImpl implements TransaccionRepository {
    private final List<Transaccion> historial = new ArrayList<>();

    @Override
    public void guardar(Transaccion transaccion) {
        historial.add(transaccion);
    }

    @Override
    public List<Transaccion> buscarPorSocio(int socioId) {
        return historial.stream()
                .filter(t -> t.prestamo().socio().getId() == socioId)
                .toList();
    }
}