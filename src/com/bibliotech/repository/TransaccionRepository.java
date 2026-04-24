package com.bibliotech.repository;

import com.bibliotech.model.Transaccion;
import java.util.List;

public interface TransaccionRepository {
    void guardar(Transaccion transaccion);
    List<Transaccion> buscarPorSocio(int socioId);
}