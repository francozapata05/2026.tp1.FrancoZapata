package com.bibliotech.repository;

import com.bibliotech.model.Transaccion;
import java.util.ArrayList;
import java.util.List;

public class TransaccionRepositoryImpl implements TransaccionRepository {
    private final List<Transaccion> transaccionRepo = new ArrayList<>();

    @Override
    public void guardar(Transaccion transaccion) {
        transaccionRepo.add(transaccion);
    }

    @Override
    public List<Transaccion> buscarPorSocio(int socioId) {
        return transaccionRepo.stream()
                .filter(t -> t.prestamo().socio().getId() == socioId)
                .toList();
    }
}