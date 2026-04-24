package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SocioRepositoryImpl implements SocioRepository {
    private final Map<String, Socio> almacen = new HashMap<>();

    @Override
    public void guardar(Socio socio) {
        almacen.put(socio.getDni(), socio);
    }

    @Override
    public Optional<Socio> buscarPorId(String dni) {
        return Optional.ofNullable(almacen.get(dni));
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(almacen.values());
    }
}
