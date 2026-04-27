package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SocioRepositoryImpl implements SocioRepository {
    private final Map<Integer, Socio> socioRepo = new HashMap<>();

    @Override
    public void guardar(Socio socio) {
        socioRepo.put(socio.getId(), socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(socioRepo.get(id));
    }

    @Override
    public Optional<Socio> buscarPorDni(String dni) {
        return socioRepo.values().stream()
                .filter(s -> s.getDni().equals(dni))
                .findFirst();
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(socioRepo.values());
    }
}
